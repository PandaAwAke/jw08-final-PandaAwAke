package com.pandaawake.gourdgame.network.data.action;

public abstract class ConnectionAction extends Action {

    public ConnectionAction(int senderClientId) {
        super(senderClientId);
    }

    public static class ClientEnter extends ConnectionAction {

        public ClientEnter(int senderClientId) {
            super(senderClientId);
        }

    }

    public static class ClientExit extends ConnectionAction {

        public ClientExit(int senderClientId) {
            super(senderClientId);
        }

    }

    public static class ClientSuccessfullyAccepted extends ConnectionAction {

        public int playerId;

        public ClientSuccessfullyAccepted(int senderClientId, int playerId) {
            super(senderClientId);
            this.playerId = playerId;
        }

    }

    public static class ClientUnsuccessfullyAccepted extends ConnectionAction {
        public String errorReason;
        public ClientUnsuccessfullyAccepted(int senderClientId, String errorReason) {
            super(senderClientId);
            this.errorReason = errorReason;
        }
    }

    public static class ServerClosed extends ConnectionAction {

        public ServerClosed(int senderClientId) {
            super(senderClientId);
        }

    }


}
