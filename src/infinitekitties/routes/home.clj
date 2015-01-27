(ns infinitekitties.routes.home
  (:require [infinitekitties.layout :as layout]
            [infinitekitties.util :as util]
            [ring.util.response :refer :all]))

(def cat-url "https://farm8.staticflickr.com/7073/7190755946_ea97e85765_b_d.jpg")

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/cat" [] (content-type (url-response (java.net.URL. cat-url)) "image/jpeg" ))
  (GET "/j" [] {:status 200 :headers {"Content-Type" "application/json"} :body "{ 'test' : 'json' }"}))
