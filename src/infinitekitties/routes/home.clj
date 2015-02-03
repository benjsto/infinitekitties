(ns infinitekitties.routes.home
  (:require [compojure.core :refer :all]
            [infinitekitties.layout :as layout]
            [ring.util.response :refer :all]
            [clj-http.client :as client]))

(def flickr-api-key "api_key=dd6677263952ea6151b3a8ab2956e25b&")
(def flickr-url "https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&")
(def flickr-search "method=flickr.photos.search&text=kitty&per_page=1&")
(def flickr-photo-sizes "method=flickr.photos.getSizes&photo_id=")
(def flickr-photo-info "method=flickr.photos.getInfo&photo_id=")

(defn get-random-page []
  (str "page=" (+ 1 (rand-int 5000)) "&"))

(defn flickr-search-url []
  (str flickr-url flickr-search (get-random-page) flickr-api-key))

(defn flickr-sizes-url [id]
  (str flickr-url flickr-api-key flickr-photo-sizes id))

(defn flickr-getinfo-url [id]
  (str flickr-url flickr-api-key flickr-photo-info id))

(defn get-some-cats-from-flickr []
  (let [search-url (flickr-search-url)]
    (client/get search-url {:as :json})))

(defn cats-body []
  (:body (get-some-cats-from-flickr)))
(defn cat-photos []
  (:photo (:photos (cats-body))))
(defn get-random-cat-id []
  (:id (get (cat-photos) 0)))

(defn get-a-random-cats-sizes-from-flickr [photo-id]
  (let [photo-id photo-id]
    (client/get (flickr-sizes-url photo-id) {:as :json})))

(defn get-photo-info [photo-id]
  (:body (client/get (flickr-getinfo-url photo-id) {:as :json})))

(defn cats-photo-body [photo-id]
  (:body (get-a-random-cats-sizes-from-flickr photo-id)))

(defn get-random-medium-cat-photo-url [photo-id]
  (:source (first (filter #(= "Medium" (get % :label)) (:size (:sizes (cats-photo-body photo-id)))))))

(defn home-page []
  (let [photo-id (get-random-cat-id)]
    (layout/render "home.html" {:cat (get-random-medium-cat-photo-url photo-id) :photo-info (get-photo-info photo-id)})))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/j" [] {:status 200 :headers {"Content-Type" "application/json"} :body "{ 'test' : 'json' }"}))
