(ns pls.core
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as s])
  (:require [clj-http.client :as client])
  (:import [java.io BufferedOutputStream FileOutputStream]))

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
        mapify-grouped-lines (fn [gl]
                               {:file (get-after-equals (first gl))
                                :title (get-after-equals (second gl))})]
    (->> lines
         (group-by entry-number)
         (vals)
         (map mapify-grouped-lines))))

(defn get-genres-from-pls-file [filename]
  (->> filename
       (parse-file)
       (map :file)
       (map (fn [url] (last (re-find #":80\/([a-z]+)\?" url))))))

(defn get-urls-from-pls-file [filename]
  (->> filename
       (parse-file)
       (map :file)))
#'pls.core/get-urls-from-pls-file

(first (get-urls-from-pls-file "/Users/tobi/Music/di.pls"))

(get-genres-from-pls-file "/Users/tobi/Music/di.pls")
("drumandbass" "liquiddubstep" "synthwave" "breaks" "progressivepsy" "detroithousentechno" "bassline" "chillout" "vocaltrance" "chillhop" "spacemusic" "chillntropicalhouse" "vocalchillout" "melodicprogressive" "oldschoolacid" "clubdubstep" "atmosphericbreaks" "progressive" "glitchhop" "darkpsytrance" "deephouse" "deepnudisco" "goapsy" "downtempolounge")

;; TODO get the binary data from the stream and display it
;; TODO write the binary data to an audio stream and then play it

(let [url (first (get-urls-from-pls-file "/Users/tobi/Music/di.pls"))
      headers (:headers (client/head url))]
  (get headers "icy-genre"))
"Drum and Bass"
"Chill & Tropical House"


;;current-time (System/currentTimeMillis)
;;output-filename (format "/Users/tobi/Desktop/%s-%d.wav" genre current-time)
;;output-stream (BufferedOutputStream. (FileOutputStream. output-filename))

(let [genre "progressive"
      url (first (get-urls-from-pls-file "/Users/tobi/Music/di.pls"))
      response (client/get url {:as :stream})
      input-stream (:body response)
      buffer (make-array Byte/TYPE 16)]
  (doall
    (.read input-stream buffer)
    (into [] buffer)))

[-1 -15 80 -128 46 31 -52 33 26 -113 -3 -6 -73 -1 -31 69]
