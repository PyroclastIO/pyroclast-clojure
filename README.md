# pyroclast-clojure

A Clojure library for sending events to a Pyroclast topic.

## Installation

With Leiningen:

```clojure
[io.pyroclast/pyroclast-clojure "0.1.6"]
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

### Pull the Docker Container

To get started, pull the Roaming Docker container and start it up.

```console
$ docker pull pyroclastio/roaming:0.1.2

$ docker run -it -p 9700:9700 pyroclastio/roaming:0.1.2
```

Wait a moment for the the uberjar to launch. When it's fully loaded, it'll echo it's configuration.

### Example: streaming ETL

For our first example, we'll work through a simple streaming ETL problem. We begin by importing
all the Roaming namespaces with functions we might use. Next, we define a configuration
endpoint. The only key that's needed is the endpoint to Roaming, which for Docker, we started
locally on port `9700`.

After the initial code is set up, we define a service that reads events
off an input topic. These events represent sentences. We'll split apart
the sentences into their individual words, split those words into their
own events, then clean them up by lowercasing them and stripping out non
alpha-numeric characters. We'll stream the results to another topic for
further processing. Finally, we add a test to ensure the service
does what it's supposed to do.

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
            [pyroclast-clojure.v1.roaming.topic :as t]))

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
    (is (:success? simulation))
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

Now for something more advanced. Now let's suppose our records are being streamed
from an IoT sensor emitting temperature readings and other data. Our first goal
is to convert the readings from fahrenheit to celsius, then clean up the digits
and output the data elsewhere. Let's see what that looks like (comments inline).

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
                 (f/= "event-type" "reading")       ;; Only interested in tempterature readings.
                 (c/parse-vals {"value" "double"})  ;; Parse strings into doubles.
                 (math/minus "value" 32)            ;; Fahrenheit to celsius math.
                 (math/divide "value" 1.8)
                 (pseq/assoc-in ["unit"] "celsius") ;; Update the event to reflect the new unit.
                 (math/round-decimals "value" 2)    ;; Tidy up our decimals.
                 (t/output-topic "as-celsius")))
```

Pretty intuitive. Let's write a test.

```clojure
(deftest test-streaming-temperature-sensor-readings
  (let [simulation (roaming/simulate! config service temperature-records)]
    (is (:success? simulation))
    (is (= [{"sensor-id" "1" "event-type" "reading" "value" 10.13 "unit" "celsius"}
            {"sensor-id" "2" "event-type" "reading" "value" 9.09 "unit" "celsius"}
            {"sensor-id" "3" "event-type" "reading" "value" 11.66 "unit" "celsius"}
            {"sensor-id" "1" "event-type" "reading" "value" 9.74 "unit" "celsius"}
            {"sensor-id" "3" "event-type" "reading" "value" 10.63 "unit" "celsius"}
            {"sensor-id" "2" "event-type" "reading" "value" 10.02 "unit" "celsius"}
            {"sensor-id" "2" "event-type" "reading" "value" 10.01 "unit" "celsius"}]
           (get-in simulation [:result :output-records])))))
```

Celsius rounded to two digits. Excellent. Let's be idiomatic and extract the unit
conversion piece of our service into its own function. Since it's just data under the
hood, this is easy.

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
                 (f-to-c "value")                   ;; Replaces inline math.
                 (math/round-decimals "value" 2)
                 (t/output-topic "as-celsius")))
```

Looks good, we've factored out the cohesive part of the service. If we test again,
we can see the service behaves the same.

```clojure
(test-streaming-temperature-sensor-readings)
```

We've verified that our unit conversion works. Let's try aggregating those readings next.
In this next portion, we replace the output topic with an aggregation. Here, we take
the minimum, maximum, and average of all readings, grouped by sensor ID. A global window
indicates that we are interested in all events, regardless of when they happened. More
on that later!

```clojure
(def service
  (-> (s/new-service)
      (t/input-topic "sensor-events")
      (f/= "event-type" "reading")
      (c/parse-vals {"value" "double"})
      (f-to-c "value")
      (a/aggregations
       [(a/min "min-reading" "value" (a/globally-windowed))
        (a/max "max-reading" "value" (a/globally-windowed))
        (a/average "avg-reading" "value" (a/globally-windowed))]
       "sensor-id")))
```

`agggregate-together` executes a collection of aggregates over a stream of events.
now for the test. Each aggregate is available by name, and we'll see our sensor
values bucketed by id.

```clojure
(deftest test-aggregated-temperature-reads-by-id
  (let [simulation (roaming/simulate! config service temperature-records)

        min-reading (get-in simulation [:result :aggregates "min-reading"])
        max-reading (get-in simulation [:result :aggregates "max-reading"])
        avg-reading (get-in simulation [:result :aggregates "avg-reading"])]

    (is (:success? simulation))

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

### Example: time-windowed aggregations

For our last example, we'll incorporate event time into our aggregations.
Here are some new records representing page views.

```clojure
(def records
  [{"page" "/console" "browser" "chrome" "country" "usa" "timestamp" "2017-07-11T16:37:08.000-00:00"}
   {"page" "/console" "browser" "chrome" "country" "br" "timestamp" "2017-07-11T16:34:02.000-00:00"}
   {"page" "/store" "browser" "chrome" "country" "usa" "timestamp" "2017-07-11T16:48:15.000-00:00"}
   {"page" "/console" "browser" "firefox" "country" "usa" "timestamp" "2017-07-11T16:01:53.000-00:00"}
   {"page" "/store" "browser" "chrome" "country" "br" "timestamp" "2017-07-11T16:05:35.000-00:00"}
   {"page" "/console" "browser" "chrome" "country" "usa" "timestamp" "2017-07-11T15:20:08.000-00:00"}
   {"page" "/about" "browser" "chrome" "country" "can" "timestamp" "2017-07-11T16:21:01.000-00:00"}
   {"page" "/console" "browser" "firefox" "country" "usa" "timestamp" "2017-07-11T16:42:15.000-00:00"}
   {"page" "/console" "browser" "chrome" "country" "usa" "timestamp" "2017-07-11T16:24:59.000-00:00"}
   {"page" "/about" "browser" "chrome" "country" "usa" "timestamp" "2017-07-11T16:37:08.000-00:00"}
   {"page" "/console" "browser" "firefox" "country" "br" "timestamp" "2017-07-11T16:33:14.000-00:00"}
   {"page" "/console" "browser" "firefox" "country" "usa" "timestamp" "2017-07-11T16:37:03.000-00:00"}
   {"page" "/store" "browser" "chrome" "country" "can" "timestamp" "2017-07-10T15:24:08.000-00:00"}])
```

To aggregate with event time, we'll need to parse our string timestamps into Unix ms since the epoch.
We use `parse-datetime` with the specified format to get that job done, then aggregate into 15 minute
fixed windows over that value, counting the instances.

```clojure
(def service
  (-> (s/new-service)
      (t/input-topic "page-views")
      (time/parse-datetime "timestamp" "YYYY-MM-dd'T'HH:mm:ss.SSSZ" {:dst "parsed-time"})
      (a/aggregations
       [(a/count "windowed-page-views" (a/fixed-windows-of 15 "minutes" "parsed-time"))])))
```

Executing a test reveals the aggregate slices into 15 minute windows with lower and upper timestamp
bounds, as well as the number of events that fell into the range.

```clojure
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

A final variation on this example groups events by country, and again by browser,
still over 15 minute windows:

```clojure
(def service
  (-> (s/new-service)
      (t/input-topic "page-views")
      (time/parse-datetime "timestamp" "YYYY-MM-dd'T'HH:mm:ss.SSSZ" {:dst "parsed-time"})
      (a/aggregations
       [(a/count "windowed-page-views" (a/fixed-windows-of 15 "minutes" "parsed-time"))]
       ["country" "browser"])))
```

Pyroclast automatically sub-groups based on each specified category: country, and then browser.

```clojure
(deftest test-page-views-over-grouped-windows
  (let [simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= {"usa" {"chrome" [{"bounds" {"lower" 1499790600000 "upper" 1499791499999} "value" 2}
                             {"bounds" {"lower" 1499791500000 "upper" 1499792399999} "value" 1}
                             {"bounds" {"lower" 1499786100000 "upper" 1499786999999} "value" 1}
                             {"bounds" {"lower" 1499789700000 "upper" 1499790599999} "value" 1}]
                   "firefox" [{"bounds" {"lower" 1499788800000 "upper" 1499789699999} "value" 1}
                              {"bounds" {"lower" 1499790600000 "upper" 1499791499999} "value" 2}]}
            "br" {"chrome" [{"bounds" {"lower" 1499790600000 "upper" 1499791499999} "value" 1}
                            {"bounds" {"lower" 1499788800000 "upper" 1499789699999} "value" 1}]
                  "firefox" [{"bounds" {"lower" 1499790600000 "upper" 1499791499999} "value" 1}]}
            "can" {"chrome" [{"bounds" {"lower" 1499789700000 "upper" 1499790599999} "value" 1}
                             {"bounds" {"lower" 1499699700000 "upper" 1499700599999} "value" 1}]}}
           (get-in simulation [:result :aggregates "windowed-page-views"])))))
```

### Show me more!

The tests are a great guide of how to use every function. Run the suite with:

```clojure
lein test :roaming
```