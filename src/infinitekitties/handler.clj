(ns infinitekitties.handler
  (:require [compojure.core :refer [defroutes]]
            [infinitekitties.routes.home :refer [home-routes]]
            [noir.util.middleware :refer [app-handler]]
            [compojure.route :as route]))

(defroutes base-routes
   (route/resources "/")
   (route/not-found "Not Found"))

(defn init [])

(defn destroy [])

(def app (app-handler [home-routes base-routes]))
