(ns pls.core-test
  (:require [clojure.test :refer :all]
            [pls.core :refer :all]))

(deftest entry-number-test
  (testing "PLS entry number is parsed correctly"
    (is (= 22 (entry-number "Title22=DI.FM - Vocal Trance")))))

(deftest entry-number-test-nonumber
  (testing "PLS entry number is parsed correctly"
    (is (= -1 (entry-number "[playlist]")))))

(deftest file-to-lines-test
  (testing "PLS file is opened and the Title= and File= lines are filtered"
    (let [expected-lines '("File1=http://prem4.di.fm:80/synthwave"
                           "Title1=DI.FM - Synthwave"
                           "File2=http://prem4.di.fm:80/melodicprogressive"
                           "Title2=DI.FM - Melodic Progressive")]

      (is (= expected-lines (file-to-lines "test/pls/test.pls"))))))

(deftest parse-file-test
  (testing "PLS file parsing test"
    (let [expected-maps [{:file "http://prem4.di.fm:80/synthwave"
                          :title "DI.FM - Synthwave"},
                         {:file "http://prem4.di.fm:80/melodicprogressive"
                          :title "DI.FM - Melodic Progressive"}]]
      (is (= expected-maps (parse-file "test/pls/test.pls"))))))

