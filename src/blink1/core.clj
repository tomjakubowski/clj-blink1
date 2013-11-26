(ns blink1.core
  (:import [com.codeminders.hidapi HIDDeviceInfo HIDManager HIDDeviceNotFoundException]
           [java.io IOException]
           [java.nio ByteBuffer])
  (:require [blink1.bytes :refer [seq-to-bytes]])
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
(def ^{:private true} blink1-cmd-set-rgb (byte \n))
(def ^{:private true} blink1-cmd-fade-rgb (byte \c))

(defn- write-seq [dev seq]
  (let [len blink1-buf-size]
    (.write dev (seq-to-bytes len (cons blink1-report-id seq)))))

(defn set-rgb [dev r g b]
  "Sets the color on dev."
  (write-seq dev [blink1-cmd-set-rgb r g b]))

(defn fade-rgb [dev ms r g b]
  (let [dms (/ ms 10)]
    (write-seq dev [blink1-cmd-fade-rgb r g b
                    (bit-shift-right dms 8)
                    (mod dms 0xff)])))

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
        (fade-rgb blink1 1000 (rand-int 255) (rand-int 255) (rand-int 255))
        (finally
          (.close blink1)
          (.release (HIDManager/getInstance)))))
    (catch HIDDeviceNotFoundException e
      (.release (HIDManager/getInstance))
      (throw (RuntimeException. "No blink(1) device found.")))
    (finally
      (.release (HIDManager/getInstance)))))

(comment
  (-main))
