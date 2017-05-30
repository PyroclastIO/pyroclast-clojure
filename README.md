# pyroclast-clojure

A Clojure library for sending events to a Pyroclast topic.

## Installation

With Leiningen:

```clojure
[io.pyroclast/pyroclast-clojure "0.1.1"]
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

#### Send one event synchronously

```clojure
(client/send-event! config {:event-type "page-visit" :page "/home" :timestamp 1495072835000})
```

#### Send a batch of events synchronously

```clojure
(client/send-events! config [{:event-type "page-visit" :page "/home" :timestamp 1495072835000}
                             {:event-type "page-visit" :page "/console" :timestamp 1495072895032}])
```

#### Send one event asynchronously

```clojure
(client/send-event-async!
  config (fn [result] (println result))
  {:event-type "page-visit" :page "store" :timestamp 1495072835000})
```

#### Send a batch of events asynchronously

```clojure
(client/send-events-async!
  config (fn [results] (println results))
  [{:event-type "page-visit" :page "store" :timestamp 1495072835000}
   {:event-type "page-visit" :page "console" :timestamp 1495072895032}])
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
