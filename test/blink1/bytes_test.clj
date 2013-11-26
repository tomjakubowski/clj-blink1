(ns blink1.bytes_test
  (:require [clojure.test :refer :all]
            [blink1.util :refer :all]))

(deftest seq-to-bytes-test
  (testing "returns a buffer of the specified length"
    (is (= 8 (count (seq-to-bytes 8 []))))
    (is (= 4 (count (seq-to-bytes 4 [])))))
  (testing "the buffer inclues the byte values for each element of the sequence, in order"
    (let [values [0 120 255 9000]
          len (count values)]
      (is (= (map #(.byteValue %) values) (into [] (seq-to-bytes len values)))))))
