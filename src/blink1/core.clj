(ns blink1.core
  (:import [com.codeminders.hidapi HIDDeviceInfo HIDManager]
           [java.io IOException])
  (:gen-class))

(defn load-hid-natives []
  (let [bits (System/getProperty "sun.arch.data.model")]
    (clojure.lang.RT/loadLibrary (str "hidapi-jni-" bits))))

(def blink1-vid 10168)
(def blink1-pid 493)

(defn blink1? [dev]
  (and
    (= blink1-vid (.getVendor_id dev))
    (= blink1-pid (.getProduct_id dev))))

(defn blink1-devices []
  (let [manager (HIDManager/getInstance)]
    (filter blink1? (.listDevices manager))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Loading HID natives...")
  (load-hid-natives)
  (print (blink1-devices))
  (println "Hello, World!"))
