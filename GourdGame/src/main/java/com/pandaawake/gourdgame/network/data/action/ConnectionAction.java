package com.pandaawake.gourdgame.network.data.action;

public abstract class ConnectionAction extends Action {


    public static class ClientEnter extends ConnectionAction {}

    public static class ClientExit extends ConnectionAction {}

    public static class ClientSuccessfullyAccepted extends ConnectionAction {}

    public static class ClientUnsuccessfullyAccepted extends ConnectionAction {
        public String errorReason;
        public ClientUnsuccessfullyAccepted(String errorReason) {
            this.errorReason = errorReason;
        }
    }

    public static class ServerClosed extends ConnectionAction {}


}
