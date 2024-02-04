package ru.test.andernam.domain.old

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.firestore.DocumentReference
import ru.test.andernam.domain.old.ipl.DatabaseRequests
import ru.test.andernam.domain.old.ipl.DownloadUploadHelp

class IDownloadUploadHelp : DownloadUploadHelp {


    override var DBRequest: DatabaseRequests? = null
    override var idClient: String? = null
    override var currentDialog: DocumentReference? = null
    override var dialogList: SnapshotStateList<Message>? = null

    override fun createDBrequestClass() {
//        DBRequest = DataBaseRequestImpl()
    }
}