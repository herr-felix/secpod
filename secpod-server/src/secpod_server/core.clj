(ns secpod-server.core
  (:require [clojure.java.io :as io])
  (:gen-class))

(import org.apache.sshd.server.SshServer)
(import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider)
(import java.util.Base64)

(def hk-file (.toPath (new java.io.File "test.ser")))


(defn publicKey->Base64 [key]
  (->> key
      .getEncoded
      (.encodeToString (Base64/getEncoder))))

(def my-pk-auth 
  (reify org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator
    (authenticate [this username pubkey session]
      (println "====================================\n" (publicKey->Base64 pubkey))
      true )))

(defn create-command [name]
  (let [cmd-state (atom {:name name})]
    (reify org.apache.sshd.server.command.Command 
      (start [_ _ env] 
        (swap! cmd-state assoc :env env)
        (spit (:out-stream @cmd-state) "Hello")
        ) 
      (destroy [_ _] 
        (println @cmd-state) )
      (setInputStream [_ in-stream] 
        (swap! cmd-state assoc :in-stream in-stream) )
      (setOutputStream [_ out-stream]
        (swap! cmd-state assoc :out-stream out-stream) )
      (setErrorStream [_ err-stream] 
        (swap! cmd-state assoc :err-stream err-stream) )
      (setExitCallback [_ _] 
        nil)
      )))

(def stream-command-factory
  (reify org.apache.sshd.server.command.CommandFactory
    (createCommand [_ _ name]
      (create-command name)
      )))

(def my-password-auth 
  (reify org.apache.sshd.server.auth.password.PasswordAuthenticator
    (authenticate [_ username password session]
      true)))

(def sshd (SshServer/setUpDefaultServer))

(defn -main [& args]
  (doto sshd
    (.setHost "127.0.0.1")
    (.setPort 3223)
    (.setKeyPairProvider (new SimpleGeneratorHostKeyProvider hk-file))

    (.setPublickeyAuthenticator my-pk-auth)
    (.setPasswordAuthenticator my-password-auth)
    (.setCommandFactory stream-command-factory)
    ;(.setShellFactory(new ProcessShellFactory))
    ; (.setKeyPairProvider (new SimpleGeneratorHostKeyProvider))
    ; (.setCommandFactory (new ScpCommandFactory)) )
  )

  (.start sshd)
  (println "Server started")
  (read-line) 
  )
