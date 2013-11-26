(ns blink1.bytes
  (:import [java.nio ByteBuffer])
  (:gen-class))

(defn seq-to-bytes [len xs]
  (let [bb (ByteBuffer/allocate len)
        buf (byte-array len)]
    (doseq [x (take len xs)]
      (.put bb (.byteValue x)))
    (.rewind bb)
    (.get bb buf)
    buf))
