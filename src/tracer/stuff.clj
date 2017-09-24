(ns tracer.stuff
  (:require
    [clojure.core.async :refer [<! chan go-loop]]
    [puget.printer :refer [cprint]]
		[clj-http.client :as client]
    [cheshire.core :as json]))

(def stuff-ch (chan))

(def ^:dynamic *tracer-config* nil)

(defn rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(comment
  {:uri "http://localhost:3451/api/debug/"
	 :prefix "123abc"
   :number 16})

(defn debug [expr]
	(let [{:keys [uri prefix id]} *tracer-config*]
		(client/put
			(str uri prefix "-" (clojure.string/replace (.toString (java.util.Date.)) " " "_") "-" (rand-str 4))
		  {:body (json/generate-string
               {:prefix prefix
                :id id
                :data ((when (string? expr) identity pr-str) expr)})
		   :headers {}
  		 :socket-timeout 1000  ;; in milliseconds
		   :conn-timeout 1000})))

(defn dbgfn [name_ f]
  (fn [& args]
    (debug (str "you are calling" name_ "with" (pr-str args)))
    (let [result (apply f args)]
      (debug (str "calling" name_ "with" (pr-str args) "returned" (pr-str result)))
      result)))
          

(defmacro dbg [x]
  (let [is-fn?
        (and
          (symbol? x)
          (try
            (do
              (resolve x)
              true)
            (catch Exception e
              (println e)
              false)))]
  (if is-fn?
    `(dbgfn '~x ~x)
    (do
      (println "macro called" (str \" x \") "on the way")
      x))))

(defmacro dbgfn [expr]
  `(do
    (debug '~expr)
    ~expr))

(go-loop
  []
	(binding [*tracer-config* {:uri "http://127.0.0.1:5984/cljdebug/"
														 :prefix "abcdef-"}]
	  (let [data1 (<! stuff-ch)]
  	  (newline)
	    (println ">>>" (.toString (new java.util.Date)))
  	  ((dbgfn cprint) "Hello world!")))
    	(recur))
