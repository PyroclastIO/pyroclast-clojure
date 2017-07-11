# pyroclast-clojure

A Clojure library for sending events to a Pyroclast topic.

## Installation

With Leiningen:

```clojure
[io.pyroclast/pyroclast-clojure "0.1.4"]
```

## Topic APIs

### Writing events

Define a configuration.

```clojure
(require '[pyroclast-clojure.v1.client :as client])

(def config
  {:write-api-key "<token>"
   :topic-id "<topic-id>"
   :format :json})
```

Events must contain a value key, which describes the message, and may also
contain a optional key key, which will be used to route particular messages to
particular topic partitions, and can be accessed for other purposes.

#### Send one event synchronously

```clojure
(client/send-event! config {:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}})
```

#### Send a batch of events synchronously

```clojure
(client/send-events! config [{:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "/console" :timestamp 1495072895032}}])
```

#### Send one event asynchronously

```clojure
(client/send-event-async!
  config (fn [result] (println result))
  {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}})
```

#### Send a batch of events asynchronously

```clojure
(client/send-events-async!
  config (fn [results] (println results))
  [{:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
   {:value {:event-type "page-visit" :page "console" :timestamp 1495072895032}}])
```

### Reading events

Define a configuration.

```clojure
(require '[pyroclast-clojure.v1.client :as client])

(def config
  {:read-api-key "<token>"
   :topic-id "<topic-id>"
   :format :json})
```

#### Subscribe to a topic

```clojure
(client/subscribe-to-topic! config "your-subscriber-name")
```

#### Poll subscribed topic

```clojure
(client/poll-topic! config "your-subscriber-name")
```

#### Commit read records

```clojure
(client/commit-read-records! config "your-subscriber-name")
```

## Deployed Service APIs

### Read deployed service aggregations

Define a configuration.

```clojure
(require '[pyroclast-clojure.v1.client :as client])

(def config
  {:read-api-key "<token>"
   :region "<region>"
   :deployment-id "<deployment-id>"})
```

#### Get all aggregates for a deployed service

```clojure
(client/read-aggregates config)
```

#### Get an aggregate by name for a deployed service

```clojure
(client/read-aggregate config "aggregate-name")
```

#### Get a single aggregate group by name

```clojure
(client/read-aggregate-group config "aggregate-name" "group-name")
```

## Roaming

Roaming is an extension of Pyroclast that virtualizes its internal streaming
engine and APIs into a Docker container. Using this SDK, Clojure can connect
to Roaming and execute streaming programs in a fast, deterministic environment,
mirroring their behavior in a Pyroclast's distributed, production-grade cloud.

### The Docker Container

```console
$ docker pull pyroclastio/roaming:0.1.1

$ docker run -it -p 9700:9700 pyroclastio/roaming:0.1.1
```

Wait a moment for the the uberjar to launch.

### Example: streaming ETL

```clojure
(ns my.project
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.seq :as pseq]
            [pyroclast-clojure.v1.roaming.string :as string]
            [pyroclast-clojure.v1.roaming.math :as math]
            [pyroclast-clojure.v1.roaming.time :as time]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.filters :as f]
            [pyroclast-clojure.v1.roaming.aggregations :as a]
            [pyroclast-clojure.v1.roaming.coerce :as c]
            [pyroclast-clojure.v1.roaming.topic :as t]
            [pyroclast-clojure.util :as u]))

(def config {:endpoint "http://127.0.0.1:9700"})

(def records [{"sentence" "Pyroclast Roaming is a tool virtualizes Pyroclast's runtime."}])

(def service (-> (s/new-service)
                 (t/input-topic "input")
                 (string/split-whitespace "sentence")
                 (pseq/explode "sentence" {:dst "word"})
                 (string/replace "word" "[^a-zA-Z0-0]" "")
                 (string/lower-case "word")
                 (t/output-topic "output")))

(deftest test-word-splitting
  (let [simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"word" "pyroclast"}
            {"word" "roaming"}
            {"word" "is"}
            {"word" "a"}
            {"word" "tool"}
            {"word" "virtualizes"}
            {"word" "pyroclasts"}
            {"word" "runtime"}]
           (get-in simulation [:result :output-records])))))
```

#### Example: Streaming aggregations

```clojure
(def temperature-records
  [{"sensor-id" "1" "event-type" "reading" "value" "50.24" "unit" "fahrenheit"}
   {"sensor-id" "2" "event-type" "reading" "value" "48.37" "unit" "fahrenheit"}
   {"sensor-id" "3" "event-type" "reading" "value" "52.98" "unit" "fahrenheit"}
   {"sensor-id" "2" "event-type" "ping"}
   {"sensor-id" "1" "event-type" "reading" "value" "49.53" "unit" "fahrenheit"}
   {"sensor-id" "3" "event-type" "reading" "value" "51.13" "unit" "fahrenheit"}
   {"sensor-id" "2" "event-type" "reading" "value" "50.03" "unit" "fahrenheit"}
   {"sensor-id" "1" "event-type" "ping"}
   {"sensor-id" "2" "event-type" "reading" "value" "50.01" "unit" "fahrenheit"}])
```

```clojure
(def service (-> (s/new-service)
                 (t/input-topic "sensor-events")
                 (f/= "event-type" "reading")
                 (c/parse-vals {"value" "double"})
                 (math/minus "value" 32)
                 (math/divide "value" 1.8)
                 (pseq/assoc-in ["unit"] "celsius")
                 (math/round-decimals "value" 2)
                 (t/output-topic "as-celsius")))
```

```clojure
(deftest test-streaming-temperature-sensor-readings
  (let [simulation (roaming/simulate! config service temperature-records)]
    (is (:success? simulation) simulation)
    (is (= [{"sensor-id" "1" "event-type" "reading" "value" 10.13 "unit" "celsius"}
            {"sensor-id" "2" "event-type" "reading" "value" 9.09 "unit" "celsius"}
            {"sensor-id" "3" "event-type" "reading" "value" 11.66 "unit" "celsius"}
            {"sensor-id" "1" "event-type" "reading" "value" 9.74 "unit" "celsius"}
            {"sensor-id" "3" "event-type" "reading" "value" 10.63 "unit" "celsius"}
            {"sensor-id" "2" "event-type" "reading" "value" 10.02 "unit" "celsius"}
            {"sensor-id" "2" "event-type" "reading" "value" 10.01 "unit" "celsius"}]
           (get-in simulation [:result :output-records])))))
```

```clojure
(defn f-to-c [service k]
  (-> service
      (math/minus k 32)
      (math/divide k 1.8)
      (pseq/assoc-in ["unit"] "celsius")))

(def service (-> (s/new-service)
                 (t/input-topic "sensor-events")
                 (f/= "event-type" "reading")
                 (c/parse-vals {"value" "double"})
                 (f-to-c "value")
                 (math/round-decimals "value" 2)
                 (t/output-topic "as-celsius")))
```

```clojure
(test-streaming-temperature-sensor-readings)
```

```clojure
(def service (-> (s/new-service)
                 (t/input-topic "sensor-events")
                 (f/= "event-type" "reading")
                 (c/parse-vals {"value" "double"})
                 (f-to-c "value")
                 (a/aggregate-together
                  [(a/min "min-reading" "value" (a/globally-windowed))
                   (a/max "max-reading" "value" (a/globally-windowed))
                   (a/average "avg-reading" "value" (a/globally-windowed))]
                  "sensor-id")))

(deftest test-aggregated-temperature-reads-by-id
  (let [config (:roaming (u/load-config "config.edn"))
        simulation (roaming/simulate! config service temperature-records)

        min-reading (get-in simulation [:result :aggregates "min-reading"])
        max-reading (get-in simulation [:result :aggregates "max-reading"])
        avg-reading (get-in simulation [:result :aggregates "avg-reading"])]

    (is (:success? simulation) simulation)

    (is (= {"1" {"value" 9.738888888888889}
            "2" {"value" 9.094444444444443}
            "3" {"value" 10.627777777777778}}
           min-reading))

    (is (= {"1" {"value" 10.133333333333335}
            "2" {"value" 10.016666666666667}
            "3" {"value" 11.655555555555553}}
           max-reading))

    (is (= {"1" {"value" 9.936111111111112}
            "2" {"value" 9.705555555555556}
            "3" {"value" 11.141666666666666}}
           avg-reading))))
```

#### Example: Time-windowed aggregations

```clojure
(def records
  [{"page" "/console" "browser" "Chrome" "country" "USA" "timestamp" "2017-07-11T16:37:08.000-00:00"}
   {"page" "/console" "browser" "Chrome" "country" "BR" "timestamp" "2017-07-11T16:34:02.000-00:00"}
   {"page" "/store" "browser" "Chrome" "country" "USA" "timestamp" "2017-07-11T16:48:15.000-00:00"}
   {"page" "/console" "browser" "Firefox" "country" "USA" "timestamp" "2017-07-11T16:01:53.000-00:00"}
   {"page" "/store" "browser" "Chrome" "country" "BR" "timestamp" "2017-07-11T16:05:35.000-00:00"}
   {"page" "/console" "browser" "Chrome" "country" "USA" "timestamp" "2017-07-11T15:20:08.000-00:00"}
   {"page" "/about" "browser" "Chrome" "country" "CAN" "timestamp" "2017-07-11T16:21:01.000-00:00"}
   {"page" "/console" "browser" "Firefox" "country" "USA" "timestamp" "2017-07-11T16:42:15.000-00:00"}
   {"page" "/console" "browser" "Chrome" "country" "USA" "timestamp" "2017-07-11T16:24:59.000-00:00"}
   {"page" "/about" "browser" "Chrome" "country" "USA" "timestamp" "2017-07-11T16:37:08.000-00:00"}
   {"page" "/console" "browser" "Firefox" "country" "BR" "timestamp" "2017-07-11T16:33:14.000-00:00"}
   {"page" "/console" "browser" "Firefox" "country" "USA" "timestamp" "2017-07-11T16:37:03.000-00:00"}
   {"page" "/store" "browser" "Chrome" "country" "CAN" "timestamp" "2017-07-10T15:24:08.000-00:00"}])
```

```clojure
(def service
  (-> (s/new-service)
      (t/input-topic "page-views")
      (time/parse-datetime "timestamp" "YYYY-MM-dd'T'HH:mm:ss.SSSZ" {:dst "parsed-time"})
      (a/aggregate-together
       [(a/count "windowed-page-views" (a/fixed-windows-of 15 "minutes" "parsed-time"))])))

(deftest test-page-views-over-fixed-windows
  (let [simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"bounds" {"lower" 1499790600000 "upper" 1499791499999} "value" 6}
            {"bounds" {"lower" 1499791500000 "upper" 1499792399999} "value" 1}
            {"bounds" {"lower" 1499788800000 "upper" 1499789699999} "value" 2}
            {"bounds" {"lower" 1499786100000 "upper" 1499786999999} "value" 1}
            {"bounds" {"lower" 1499789700000 "upper" 1499790599999} "value" 2}
            {"bounds" {"lower" 1499699700000 "upper" 1499700599999} "value" 1}]
           (get-in simulation [:result :aggregates "windowed-page-views"])))))
```

```clojure
(def service
  (-> (s/new-service)
      (t/input-topic "page-views")
      (time/parse-datetime "timestamp" "YYYY-MM-dd'T'HH:mm:ss.SSSZ" {:dst "parsed-time"})
      (a/aggregate-together
       [(a/count "windowed-page-views" (a/fixed-windows-of 15 "minutes" "parsed-time"))]
       ["country" "browser"])))

(deftest test-page-views-over-grouped-windows
  (let [simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= {"USA" {"Chrome" [{"bounds" {"lower" 1499790600000 "upper" 1499791499999} "value" 2}
                             {"bounds" {"lower" 1499791500000 "upper" 1499792399999} "value" 1}
                             {"bounds" {"lower" 1499786100000 "upper" 1499786999999} "value" 1}
                             {"bounds" {"lower" 1499789700000 "upper" 1499790599999} "value" 1}]
                   "Firefox" [{"bounds" {"lower" 1499788800000 "upper" 1499789699999} "value" 1}
                              {"bounds" {"lower" 1499790600000 "upper" 1499791499999} "value" 2}]}
            "BR" {"Chrome" [{"bounds" {"lower" 1499790600000 "upper" 1499791499999} "value" 1}
                            {"bounds" {"lower" 1499788800000 "upper" 1499789699999} "value" 1}]
                  "Firefox" [{"bounds" {"lower" 1499790600000 "upper" 1499791499999} "value" 1}]}
            "CAN" {"Chrome" [{"bounds" {"lower" 1499789700000 "upper" 1499790599999} "value" 1}
                             {"bounds" {"lower" 1499699700000 "upper" 1499700599999} "value" 1}]}}
           (get-in simulation [:result :aggregates "windowed-page-views"])))))
```