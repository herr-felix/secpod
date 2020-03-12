(ns secpod-server.core
  (:gen-class))

(import org.apache.sshd.server.SshServer)
(import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider)

(def hk-file (.toPath (new java.io.File "test.ser")))

(def my-pk-auth 
  (reify org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator
    (authenticate [this username pubkey session]
      (println username)
      (println (.getEncoded pubkey))
      true)))

(def stream-command-factory
  (reify org.apache.sshd.server.command.CommandFactory
    (createCommand [_ _ name]
      (println name)
      (reify org.apache.sshd.server.command.Command 
        (start [_ _ env] (println name) (println env)) 
        (destroy [_ _] (println "fini"))
        (setErrorStream [_ _] nil)
        (setExitCallback [_ _] nil)
        (setInputStream [_ _] nil)
        (setOutputStream [_ _] nil)
        ))))

(def my-password-auth 
  (reify org.apache.sshd.server.auth.password.PasswordAuthenticator
    (authenticate [_ username password session]
      (println username)
      (println password)
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
