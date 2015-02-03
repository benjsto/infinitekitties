(ns infinitekitties.routes.home
  (:require [compojure.core :refer :all]
            [infinitekitties.layout :as layout]
            [infinitekitties.util :as util]
            [ring.util.response :refer :all]
            [clj-http.client :as client]))

(def flickr-api-key "api_key=dd6677263952ea6151b3a8ab2956e25b&")
(def flickr-url "https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&")
(def flickr-search "method=flickr.photos.search&tags=cat&per_page=100&")
(def flickr-photo-sizes "method=flickr.photos.getSizes&photo_id=")

(defn get-random-page [] (str "page=" (rand-int 50) "&"))

(defn flickr-search-url [] (str flickr-url flickr-search (get-random-page) flickr-api-key))

(defn flickr-sizes-url [id] (str flickr-url flickr-api-key flickr-photo-sizes id))

(defn get-some-cats-from-flickr []
  (client/get (flickr-search-url) {:as :json}))

(defn cats-body [] (:body (get-some-cats-from-flickr)))
(defn cat-photos [] (:photo (:photos (cats-body))))
(defn get-random-cat-id [] (:id (get (cat-photos) (rand-int 99))))

(defn get-a-random-cats-sizes-from-flickr []
  (client/get (flickr-sizes-url (get-random-cat-id)) {:as :json}))

(defn cats-photo-body [] (:body (get-a-random-cats-sizes-from-flickr)))
(defn get-random-medium-cat-photo-url []
  (:source (first (filter #(= "Large" (get % :label)) (:size (:sizes (cats-photo-body)))))))

(defn home-page []
  (layout/render "home.html" {:cat (get-random-medium-cat-photo-url)}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/cat" [] (content-type (url-response (java.net.URL. (get-random-medium-cat-photo-url))) "image/jpeg" ))
  (GET "/j" [] {:status 200 :headers {"Content-Type" "application/json"} :body "{ 'test' : 'json' }"}))
