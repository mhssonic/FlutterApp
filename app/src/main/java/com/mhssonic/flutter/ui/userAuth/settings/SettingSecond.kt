package com.mhssonic.flutter.ui.userAuth.settings

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.UserSignUpData
import com.mhssonic.flutter.model.UserUri
import com.mhssonic.flutter.service.http.ApiService
import com.mhssonic.flutter.service.http.RetrofitInstance
import com.mhssonic.flutter.ui.userAuth.sign_up.SignUp
import com.mhssonic.flutter.ui.userAuth.sign_up.SignUpFourth
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Calendar
import java.util.Locale


class SettingSecond : SignUp() {
    private lateinit var btnRegisterFragment: Button
    private lateinit var spinnerCountry: Spinner
    private lateinit var edBirthDate: EditText
    private var selectedDay: Int = 0
    private var selectedMonth: Int = 0
    private var selectedYear: Int = 0
    private lateinit var compositeDisposable: CompositeDisposable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_setting_second, container, false)
        spinnerCountry = view.findViewById(R.id.spCountry)
        btnRegisterFragment = view.findViewById(R.id.btnRegister)
        compositeDisposable = CompositeDisposable()

        val countryList = getCountryList()
        countryList.add(0, "کشور")


        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countryList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCountry.adapter = adapter

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCountry = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                emptyToast()
            }
        }
        edBirthDate = view.findViewById(R.id.edBirthDate)
        edBirthDate.setOnClickListener {
            showDate()
        }


        var birthDate = "$selectedDay-$selectedMonth-$selectedYear\""

        val serviceApi = RetrofitInstance.getApiService(requireActivity().getSharedPreferences("cookies",
            AppCompatActivity.MODE_PRIVATE
        ))

        btnRegisterFragment.setOnClickListener {button ->
            button.isEnabled = false
            val fourthFragment = SignUpFourth()

            val bundle = Bundle()
            fourthFragment.arguments = bundle
            Log.i("MYTAG", "$birthDate")


            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fourthFragment)
                .addToBackStack(null)
                .commit()




            val userSignUpData = UserSignUpData()
            val userUri = arguments?.getSerializable("userUri") as? UserUri

            if (userUri != null) {
                val headerUri = userUri.headerUri
                val avatarUri = userUri.avatarUri
                // Use the properties as needed
            }

            val handler = Handler(Looper.getMainLooper())

            val uriAvatarAttachment : MutableLiveData<Int> = MutableLiveData(null)
            val uriHeaderAttachment: MutableLiveData<Int> = MutableLiveData(null)

            uriHeaderAttachment.observe(requireActivity(), Observer {
                if(uriAvatarAttachment.value != null && it != null) {
                    userSignUpData.avatar = uriAvatarAttachment.value
                    userSignUpData.header = it
                    updateUser(serviceApi, userSignUpData, handler, button)
                }
            })

            uriAvatarAttachment.observe(requireActivity(), Observer {
                if(uriHeaderAttachment.value != null && it != null) {
                    userSignUpData.header = uriHeaderAttachment.value
                    userSignUpData.avatar = it
                    updateUser(serviceApi, userSignUpData, handler, button)
                }
            })

//            UploadFileService.uploadFile(uriAvatar, requireContext().contentResolver, serviceApi, uriAvatarAttachment, compositeDisposable)
//            UploadFileService.uploadFile(uriHeader, requireContext().contentResolver, serviceApi, uriHeaderAttachment, compositeDisposable)

        }
        return view
    }

    private fun updateUser(serviceApi: ApiService, userSignUpData : UserSignUpData, handler: Handler, view: View){
        compositeDisposable.add(serviceApi.updateProfile(userSignUpData).subscribeOn(Schedulers.io()).subscribe({
            handler.post {
                val responseBody = it.string()
                Toast.makeText(requireContext(), responseBody, Toast.LENGTH_SHORT).show()
                view.isEnabled = true
            }
        }, {
            handler.post {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                view.isEnabled = true
            }
            Log.v("MYTAG", "failed ${it.message!!}")
        }))
    }

    private fun showDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                this.selectedDay = selectedDayOfMonth
                this.selectedMonth = selectedMonth + 1
                this.selectedYear = selectedYear
                val formattedDate = "${selectedDayOfMonth}/${selectedMonth + 1}/${selectedYear}"
                edBirthDate.setText(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

    private fun getCountryList(): MutableList<String> {
        val localeList = Locale.getAvailableLocales()
        val countryList = mutableListOf<String>()
        for (locale in localeList) {
            val country = locale.displayCountry
            if (country.isNotEmpty() && !countryList.contains(country)) {
                countryList.add(country)
            }
        }

        return countryList.sorted().toMutableList()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}