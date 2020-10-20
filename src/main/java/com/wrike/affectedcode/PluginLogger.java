package com.wrike.affectedcode;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * @author daniil.timashov on 27.07.2020
 */
public class PluginLogger {

    private static final String GROUP_LOGS_NAME = "Affected code plugin";

    public static void info(String message) {
        Notifications.Bus.notify(new Notification(GROUP_LOGS_NAME, "Success", message, NotificationType.INFORMATION));
    }

    public static void warn(String message) {
        Notifications.Bus.notify(new Notification(GROUP_LOGS_NAME, "Warning", message, NotificationType.WARNING));
    }

    public static void error(String message) {
        Notifications.Bus.notify(new Notification(GROUP_LOGS_NAME, "Error", message, NotificationType.ERROR));
    }
}
