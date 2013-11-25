(ns blink1.core
  (:import [com.codeminders.hidapi HIDDeviceInfo HIDManager HIDDeviceNotFoundException]
           [java.io IOException]
           [java.nio ByteBuffer])
  (:gen-class))

(defn- load-hid-natives []
  (let [bits (System/getProperty "sun.arch.data.model")]
    (clojure.lang.RT/loadLibrary (str "hidapi-jni-" bits))))

(def ^{:private true} blink1-vid 10168)
(def ^{:private true} blink1-pid 493)

(defn open-blink1 []
  (.openById (HIDManager/getInstance) blink1-vid blink1-pid nil))

(def ^{:private true} blink1-report-id 1)
(def ^{:private true} blink1-buf-size 9)
(def ^{:private true} blink1-cmd-set-rgb (first (.getBytes "n")))

(defn set-rgb [dev r g b]
  (let [buflen blink1-buf-size
        bb (ByteBuffer/allocate buflen)
        buf (byte-array buflen)]
    (doto bb
      (.put (.byteValue blink1-report-id))
      (.put (.byteValue blink1-cmd-set-rgb))
      (.put (.byteValue r))
      (.put (.byteValue g))
      (.put (.byteValue b))
      (.rewind)
      (.get buf))
    (println (map #(bit-and % 255) (into [] buf)))
    (.write dev buf)
    dev))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Loading HID natives...")
  (load-hid-natives)
  (println "opening blink(1)...")
  (try 
    (if-let [blink1 (open-blink1)]
      (try
        (println (str "blink1: (" blink1 ")"))
        (set-rgb blink1 0 255 0)
        (finally
          (.close blink1)
          (.release (HIDManager/getInstance)))))
    (catch HIDDeviceNotFoundException e
      (.release (HIDManager/getInstance))
      (throw (RuntimeException. "No blink(1) device found.")))
    (finally
      (.release (HIDManager/getInstance))))
  (println "Hello, World!"))

(comment
  (-main))
