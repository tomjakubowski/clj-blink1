(ns blink1.bytes
  (:import [java.nio ByteBuffer])
  (:gen-class))

(defn seq-to-bytes [len seq]
  (let [bb (ByteBuffer/allocate len)
        buf (byte-array len)]
    (reduce #(.put %1 (.byteValue %2)) bb (take len seq))
    (.rewind bb)
    (.get bb buf)
    buf))
