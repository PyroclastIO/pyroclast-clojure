# pyroclast-clojure

A Clojure library for sending events to a Pyroclast topic.

## Installation

With Leiningen:

```clojure
[io.pyroclast/pyroclast-clojure "0.1.0"]
```

## Topic APIs

First, define a configuration.

```clojure
(require '[pyroclast-clojure.v1.client :as client])

(def config
  {:write-api-key "<token>"
   :endpoint "<endpoint>"
   :topic-id "<topic-id>"
   :format :json})
```

### Send one event synchronously

```clojure
(client/send-event! config {:event-type "page-visit" :page "/home" :timestamp 1495072835000})
```

### Send a batch of events synchronously

```clojure
(client/send-events! config [{:event-type "page-visit" :page "/home" :timestamp 1495072835000}
                             {:event-type "page-visit" :page "/console" :timestamp 1495072895032}])
```

### Send one event asynchronously

```clojure
(client/send-event-async!
  config (fn [result] (println result))
  {:event-type "page-visit" :page "store" :timestamp 1495072835000})
```

### Send a batch of events asynchronously

```clojure
(client/send-events-async!
  config (fn [results] (println results))
  [{:event-type "page-visit" :page "store" :timestamp 1495072835000}
   {:event-type "page-visit" :page "console" :timestamp 1495072895032}])
```

## Service APIs

First, define a configuration.

```clojure
(require '[pyroclast-clojure.v1.client :as client])

(def config
  {:read-api-key "<token>"
   :endpoint "<endpoint>"
   :service-id "<service-id>"})
```

### Get all aggregates for a service

```clojure
(client/read-aggregates config)
```

### Get an aggregate by name for a service

```clojure
(client/read-aggregate config "aggregate-name")
```

### Get a single aggregate group by name

```clojure
(client/read-aggregate-group config "aggregate-name" "group-name")
```