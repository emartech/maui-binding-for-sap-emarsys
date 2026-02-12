package com.emarsys.maui;

import androidx.annotation.NonNull;
import com.emarsys.Emarsys;
import com.emarsys.maui.mapper.MessageMapper;
import java.util.List;
import java.util.Map;

public class DotnetEmarsysInbox {

    public interface FetchMessagesResultCallback {
        void onResult(List<Map<String, Object>> messages, Throwable error);
    }

    public static void fetchMessages(@NonNull FetchMessagesResultCallback resultCallback) {
        Emarsys.getMessageInbox().fetchMessages(result -> {
            if (result.getErrorCause() != null) {
                resultCallback.onResult(null, result.getErrorCause());
            } else {
                if (result.getResult() != null) {
                    List<Map<String, Object>> mappedMessages = MessageMapper.map(result.getResult().getMessages());
                    resultCallback.onResult(mappedMessages, null);
                } else {
                    resultCallback.onResult(List.of(), null);
                }
            }
        });
    }

    public static void addTag(@NonNull String tag, @NonNull String messageId, @NonNull CompletionListener completionListener) {
        Emarsys.getMessageInbox().addTag(tag, messageId, completionListener::onCompleted);
    }

    public static void removeTag(@NonNull String tag, @NonNull String messageId, @NonNull CompletionListener completionListener) {
        Emarsys.getMessageInbox().removeTag(tag, messageId, completionListener::onCompleted);
    }

}
