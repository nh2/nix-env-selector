(ns config
  (:require [vscode.workspace :as workspace]
            [vscode-variables :as vscodeVariables]))

(defonce config (atom {}))

(defonce vscode-config  (workspace/get-configuration))

(defn update-config! []
  (let [workspace-root (first (workspace/get-folders))]
    (reset! config {:workspace-root workspace-root
                    :nix-file       (-> (workspace/config-get vscode-config :nix-env-selector/nix-file)
                                        (#(when %1 (vscodeVariables %1))))
                    :suggest-nix?   (workspace/config-get vscode-config :nix-env-selector/suggestion)
                    :nix-packages   (workspace/config-get vscode-config :nix-env-selector/packages)
                    :nix-args       (-> (workspace/config-get vscode-config :nix-env-selector/args)
                                        (#(when %1 (vscodeVariables %1))))
                    :nix-shell-path (-> (workspace/config-get vscode-config :nix-env-selector/nix-shell-path)
                                        (#(when %1 (vscodeVariables %1))))})))

(workspace/on-config-change update-config!)
