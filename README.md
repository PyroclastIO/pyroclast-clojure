# pyroclast-clojure

A Clojure library for sending events to a Pyroclast topic.

## Installation

With Leiningen:

```clojure
[io.pyroclast/pyroclast-clojure "0.2.0"]
```

## Topic APIs

### Writing events

Events must contain a value key, which describes the message, and may also
contain a optional `:key` key, which will be used to route particular messages to
particular topic partitions, and can be accessed for other purposes. Events are
converted to json before sending, which is lossy for namespaced keys.

```clojure
(require '[pyroclast-clojure.v1.client :as client])

(def config
  {:pyroclast.topic/id "topic-35e284dc-8b0c-4866-b33b-8895c25ff7f6"
   :pyroclast.topic/write-key "d8b5869d-69a9-4935-9a35-cd0f67379124"
   :pyroclast.api/region "us-west-2"})

;; Send a single event asynchronously, returning a promise
(def resp (topic-send-event! config {:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}))
;; Use clojure.core/deref to block on promise resolution
@resp ;; => true
;; Can use deref/@ inline for a synchronous operation.
@(topic-send-event! config {:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}) ;; => true

;; Batch events together for better performance
@(topic-send-events! config [{:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "/home" :timestamp 1495072835032}}]) ;; => true

```

### Reading events

Read events from Pyroclast Topics.

```clojure
(require '[pyroclast-clojure.v1.client :as client])

(def config
  {:pyroclast.topic/read-key "db621c74-0d8d-41ed-b4e2-d4279e8b46f8"
   :pyroclast.topic/id "topic-35e284dc-8b0c-4866-b33b-8895c25ff7f6"
   :pyroclast.api/region "us-west-2"})

;; Subscribe to a topic, registering a new Consumer Group
(def consumer-instance-map (client/topic-subscribe config "my-consumer-group"))
;; => {:group-id my-consumer-group, :consumer-instance-id 4e5f319b-4f97-4556-b0b6-aa9a9ff087f3}
;;
;; Poll the topic using the consumer instance map returned from topic-subscribe.
;; Note that JSON records are returned.
@(client/topic-consumer-poll! consumer-instance-map)
;; => [{"value" {"event-type" "page-visit" "page" "/home" "timestamp" 1495072835000}}
;;     {"value" {"event-type" "page-visit" "page" "/home" "timestamp" 1495072835032}}]



;; Subsequent topic-consumer-poll!'s return nothing, since we've already consumed up to
;; the largest offset on our consumer instance.
@(client/topic-consumer-poll! consumer-instance-map)
;; => []

;; Reset back to the beginning with
(client/topic-consumer-seek-beginning consumer-instance-map) ;; => true

;; Polling returns results again.
@(client/topic-consumer-poll! consumer-instance-map)
;; => [{"value" {"event-type" "page-visit" "page" "/home" "timestamp" 1495072835000}}
;;     {"value" {"event-type" "page-visit" "page" "/home" "timestamp" 1495072835032}}]



;; Commit the highest offset the consumer has read from, so consumer
;; instances on the consumer group "my-consumer-group" will start
;; reading after this commit.
(client/topic-consumer-commit-offsets consumer-instance-map)
;; => true

;; New consumer instances start reading after the last commit.
(def new-consumer-instance (topic-subscribe config "my-consumer-group"))
@(client/topic-consumer-poll! new-consumer-instance)
;; => []

;; Get information about this consumer, including its current position:
(client/topic-consumer-information consumer-instance-map)
;; => [[group-id my-subscriber-group] [consumer-instance-id 5c27ce03-63a7-4edb-84d6-b5c1236cb9f4] [positions {1 94, 0 94}]] 

```
