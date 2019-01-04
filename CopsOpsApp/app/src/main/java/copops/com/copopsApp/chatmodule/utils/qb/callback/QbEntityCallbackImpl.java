package copops.com.copopsApp.chatmodule.utils.qb.callback;

import android.os.Bundle;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

public class QbEntityCallbackImpl<T> implements QBEntityCallback<T> {

    public QbEntityCallbackImpl() {
    }

    @Override
    public void onSuccess(T result, Bundle bundle) {

    }

    @Override
    public void onError(QBResponseException e) {

    }
}
