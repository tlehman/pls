(ns pls.core
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as s])
  (:require [clj-http.client :as client])
  (:import [java.io BufferedOutputStream
            FileOutputStream])
  (:import [javax.sound.sampled AudioFormat
            AudioSystem LineUnavailableException
            SourceDataLine DataLine DataLine$Info]))


(defn file-to-lines [filename]
  "Read a PLS file and filter only the TitleN= and FileN= lines"
  (filter
   (fn [s] (re-find #"^(Title|File)" s))
   (with-open [rdr (io/reader filename)]
     (doall
      (line-seq rdr)))))

(defn entry-number [line]
  "PLS files have an entry number like File1 or Title1, non-entries return -1"
  (let [n (second (re-find #"(\d+)?=" line))]
    (if (nil? n)
      -1
      (Integer/parseInt n))))


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

(first (get-urls-from-pls-file "/Users/tobi/Music/di.pls"))

(get-genres-from-pls-file "/Users/tobi/Music/di.pls")

;; DONE get the binary data from the stream and display it
;; TODO write the binary data to an audio stream and then play it

(let [url (first (get-urls-from-pls-file "/Users/tobi/Music/di.pls"))
      headers (:headers (client/head url))]
  (get headers "icy-genre"))

;;current-time (System/currentTimeMillis)

(defn write-binary [filename bytes]
  "Write `bytes` to a file called `filename`"
  (let [ba (byte-array bytes)]
    (with-open [os (FileOutputStream. filename)]
      (.write os ba))))


(defn save-file-to-hard-drive []
  (let [genre "progressive"
        url (first
             (get-urls-from-pls-file
              "/Users/tobi/Music/di.pls"))       ;; get URL of Icecast stream
        response (client/get url {:as :stream})  ;; HTTP request the URL
        input-stream (:body response)
        buffer (make-array Byte/TYPE 2000000)    ;; Make 2MB byte buffer
        output-filename (s/join
                         ["/Users/tobi/Desktop/"
                          (str (System/currentTimeMillis))
                          ".aac"])]              ;; reate an output file

    (.read input-stream buffer)                  ;; read 2MB of response
    (write-binary output-filename
                  (byte-array buffer))))         ;; write 2MB out to filesystem



