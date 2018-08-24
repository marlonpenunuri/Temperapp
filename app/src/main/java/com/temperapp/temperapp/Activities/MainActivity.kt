package com.temperapp.temperapp.Activities

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.temperapp.temperapp.R
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class MainActivity : AppCompatActivity() {
    private var textInput : EditText? = null
    private var buttonConvert : Button? = null
    private var textResult : TextView? = null
    private var layoutResult : LinearLayout? = null
    private var progressBar : ProgressBar? = null

    private val NAMESPACE = "http://www.w3schools.com/xml/"
    private val URL  = "http://www.w3schools.com/xml/tempconvert.asmx"
    private val SOAP_ACTION = "http://www.w3schools.com/xml/CelsiusToFahrenheit"
    private val METHOD_NAME = "CelsiusToFahrenheit"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textInput = findViewById(R.id.text_input)
        buttonConvert = findViewById(R.id.button_convert)
        textResult = findViewById(R.id.text_result)
        layoutResult = findViewById(R.id.layout_result)
        progressBar = findViewById<ProgressBar>(R.id.login_progress)


        buttonConvert!!.setOnClickListener {
            val gradesCelsius = textInput!!.text.toString()

           mAsyncTask().execute(gradesCelsius)

        }

    }

    inner class mAsyncTask : AsyncTask<String, Void, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar!!.visibility = View.VISIBLE
            layoutResult!!.visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String {
            var response: String? = null
            val Request = SoapObject(NAMESPACE, METHOD_NAME)
            Request.addProperty("Celsius", p0[0])

            val soapEnvelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            soapEnvelope.dotNet = true
            soapEnvelope.setOutputSoapObject(Request)

            val androidHttpTransport = HttpTransportSE(URL)
            // this is the actual part that will call the webservice
            try {
                androidHttpTransport.call(SOAP_ACTION, soapEnvelope)
                val result = soapEnvelope.bodyIn as SoapObject
                response = result.getProperty(0).toString()
                textResult!!.text = response
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            return response!!
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressBar!!.visibility = View.GONE
            layoutResult!!.visibility = View.VISIBLE

        }
    }
}
