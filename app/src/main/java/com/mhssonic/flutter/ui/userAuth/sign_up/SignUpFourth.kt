package com.mhssonic.flutter.ui.userAuth.sign_up

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.FragmentSignUpFourthBinding
import com.mhssonic.flutter.model.UserSignUpData
import com.mhssonic.flutter.service.http.RetrofitInstance
import com.mhssonic.flutter.ui.userAuth.login.LoginActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Locale


class SignUpFourth() : SignUpFragment() {
    private lateinit var btnRegisterFragment: Button
    private lateinit var btnPreFragment: Button

    private lateinit var spinnerCountry: Spinner
    private lateinit var edBirthDate: EditText
    private var selectedDay: Int = 0
    private var selectedMonth: Int = 0
    private var selectedYear: Int = 0
    private var selectedCountry = ""

    private lateinit var binding : FragmentSignUpFourthBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpFourthBinding.inflate(layoutInflater)

        val view = inflater.inflate(R.layout.fragment_sign_up_fourth, container, false)

        btnRegisterFragment = view.findViewById(R.id.btRegister)
        btnPreFragment = view.findViewById(R.id.brPrev)

        spinnerCountry = view.findViewById(R.id.spinnerCountry)

        val countryList = getCountryList()
        countryList.add(0,"کشور")


        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countryList)
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCountry.adapter = adapter

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                 val selectedCountry = parent?.getItemAtPosition(position).toString()
                selectedCountry = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                emptyToast()
            }
        }

        val serviceApi = RetrofitInstance
            .getApiService(requireActivity()
            .getSharedPreferences("cookies", AppCompatActivity.MODE_PRIVATE))

        val intentLogin = Intent(requireActivity(), LoginActivity::class.java)

        edBirthDate = view.findViewById(R.id.editTextBirthDate)
        edBirthDate.setOnClickListener {
            showDate()
        }

        btnRegisterFragment.isEnabled = true//TODO really??
        btnRegisterFragment.setOnClickListener {
            var birthDate = "$selectedYear-$selectedMonth-$selectedDay"
            val fourthFragment = SignUpFourth()

            val bundle = Bundle()
            fourthFragment.arguments = bundle

            val user = arguments?.getSerializable("user") as? UserSignUpData
            if (user != null) {
                user.birthdate = birthDate
                user.country = selectedCountry
                user.confirmPassword = user.password

                val locales = Locale.getAvailableLocales()
                for (locale in locales) {
                    if (selectedCountry.equals(locale.displayCountry, ignoreCase = true)) {
                        user.country = locale.country
                    }
                }

                btnRegisterFragment.isEnabled = false


                val call = serviceApi.signUp(user)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val bodyString = response.body()?.string()
                            if(bodyString == "SUCCESS"){
                                requireActivity().finish()
                                startActivity(intentLogin)
                            } else {
                                //TODO make a good toast for every error
                                emptyToast(bodyString)
                            }

                        }else{
                            emptyToast()
                        }
                        btnRegisterFragment.isEnabled = true
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        btnRegisterFragment.isEnabled = true
                        //TODO change it to there is a problem with your network
                        emptyToast("An error occurred: ${t.message}")
                    }
                })
            }
        }

        btnPreFragment.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
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
                val formattedDate = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
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
}