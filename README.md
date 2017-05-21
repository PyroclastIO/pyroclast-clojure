# pyroclast-clojure

A Clojure library for sending events to a Pyroclast topic.

## Usage

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


## License

Copyright © 2017 Distibuted Masonry

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
