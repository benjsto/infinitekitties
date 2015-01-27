(ns infinitekitties.test.handler
  (:use clojure.test
        ring.mock.request
        infinitekitties.handler))

(defn do-tests [] (run-tests))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "json route"
    (let [response (app (request :get "/j"))]
      (is (= 200 (:status response)))))

  (testing "json route"
    (let [response (app (request :get "/cat"))]
      (is (= 200 (:status response)))))

  (testing "404"
    (let [response (app (request :get "/nothing"))]
      (is (= 404 (:status response))))))
