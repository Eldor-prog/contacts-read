package uz.eldor.contactreadapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentResolverCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val REQ_CODE = 10011

class MainActivity : AppCompatActivity() {
    private val adapter by lazy(LazyThreadSafetyMode.NONE) { ContactAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpListContacts()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQ_CODE)
            } else {
                loadContacts()
            }
        } else {
            loadContacts()

        }
    }

    private fun setUpListContacts() {
        recycler_view.adapter = adapter
    }

    private fun loadContacts() {
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_URI,

            )
        val contactList = mutableListOf<Contact>()
        val contactMap = hashMapOf<String, Contact>()
        ContentResolverCompat.query(
            this.contentResolver,
            ContactsContract.Contacts.CONTENT_URI,
            projection, null, null, null, null
        ).use { cursor ->

            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val photoIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)

            var name: String?
            var id: String?
            var photo: String?
            while (cursor.moveToNext()) {
                id = cursor.getString(idIndex)
                name = cursor.getString(nameIndex)
                photo = cursor.getString(photoIndex)
                val contact = Contact(name, photo, id)
                contactList.add(contact)
                contactMap[id] = contact
            }
        }
        adapter.submitList(contactList)

        loadNumbers(contactMap)

    }

    private fun loadNumbers(contactMap: HashMap<String, Contact>) {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        ContentResolverCompat.query(
            this.contentResolver, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection, null, null, null, null
        ).use { cursorNUmber ->

            val idIndex = cursorNUmber.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            )
            val numberIndex = cursorNUmber.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
            )

            var number: String?
            var id: String?
            var contact: Contact?
            val phone: Number
            while (cursorNUmber.moveToNext()) {
                id = cursorNUmber.getString(idIndex)
                contact = contactMap[id]
                if (contact != null) {
                    number = cursorNUmber.getString(numberIndex)
                    val phone = Number(number)
                    contact.numbers.add(phone)
                }

            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        }
    }
}