(ns pls.core
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as s]))


(defn entry-number [line]
  "PLS files have an entry number like File1 or Title1, non-entries return -1"
  (let [n (second (re-find #"(\d+)?=" line))]
    (if (nil? n)
      -1
      (Integer/parseInt n))))

(defn file-to-lines [filename]
  "Read a PLS file and filter only the TitleN= and FileN= lines"
  (filter
   (fn [s] (re-find #"^(Title|File)" s))
   (with-open [rdr (io/reader filename)]
     (doall
      (line-seq rdr)))))

(defn parse-file [filename]
  "Parse a PLS file into a list of {:title :file} maps"
  (let [lines (file-to-lines filename)
        get-after-equals (fn [gl] (second (s/split gl #"=")))
        mapify-grouped-lines (fn [gl] {:file (get-after-equals (first gl)) :title (get-after-equals (second gl))})]
    (->> lines
         (group-by entry-number)
         (vals)
         (map mapify-grouped-lines))))
