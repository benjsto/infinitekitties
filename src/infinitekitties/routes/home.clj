(ns infinitekitties.routes.home
  (:require [compojure.core :refer :all]
            [infinitekitties.layout :as layout]
            [ring.util.response :refer :all]
            [clj-http.client :as client]))

(def flickr-api-key "api_key=dd6677263952ea6151b3a8ab2956e25b&")
(def flickr-url "https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&")
(def flickr-search "method=flickr.photos.search&text=kitty&per_page=10&")
(def flickr-photo-sizes "method=flickr.photos.getSizes&photo_id=")
(def flickr-photo-info "method=flickr.photos.getInfo&photo_id=")

(defn get-random-page [] (str "page=" (+ 10000 (rand-int 22000)) "&"))

(defn flickr-search-url [] (str flickr-url flickr-search (get-random-page) flickr-api-key))

(defn flickr-sizes-url [id] (str flickr-url flickr-api-key flickr-photo-sizes id))

(defn flickr-getinfo-url [id] (str flickr-url flickr-api-key flickr-photo-info id))

(defn get-some-cats-from-flickr []
  (let [search-url (flickr-search-url)]
    ;(println search-url)
    (client/get search-url {:as :json})))

(defn cats-body [] (:body (get-some-cats-from-flickr)))
(defn cat-photos [] (:photo (:photos (cats-body))))
(defn get-random-cat-id [] (:id (get (cat-photos) (rand-int 10))))

(defn get-a-random-cats-sizes-from-flickr []
  (let [photo-id (get-random-cat-id)]
    ;(println photo-id)
    ;(println (client/get (flickr-getinfo-url photo-id) {:as :json}))
    (client/get (flickr-sizes-url photo-id) {:as :json})))

(defn cats-photo-body [] (:body (get-a-random-cats-sizes-from-flickr)))
(defn get-random-medium-cat-photo-url []
  (:source (first (filter #(= "Medium" (get % :label)) (:size (:sizes (cats-photo-body)))))))

(defn home-page []
  (layout/render "home.html" {:cat (get-random-medium-cat-photo-url)}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/j" [] {:status 200 :headers {"Content-Type" "application/json"} :body "{ 'test' : 'json' }"}))
