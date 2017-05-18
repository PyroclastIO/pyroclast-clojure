# pyroclast-clojure

A Clojure library for sending events to a Pyroclast topic.

## Usage

```clojure
(require '[pyroclast-clojure.client :as client])

(def config
  {:user-token "<your user token>"
   :api-token "<your api token>"
   :endpoint "<pyroclast endpoint>"
   :topic-id "<your topic id>"
   :format :json})

(client/send-event config {:event-type "page-visit" :page "/home" :timestamp 1495072835000})
```

## License

Copyright © 2017 Distibuted Masonry

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
