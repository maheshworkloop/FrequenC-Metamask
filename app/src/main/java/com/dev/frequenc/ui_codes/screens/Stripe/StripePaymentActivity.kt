package com.dev.frequenc.ui_codes.screens.Stripe

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.dev.frequenc.R
import com.dev.frequenc.ui_codes.data.AudienceDataResponse
import com.dev.frequenc.ui_codes.data.EventResponse
import com.dev.frequenc.ui_codes.data.EventTicket
import com.dev.frequenc.ui_codes.data.InitiatePaymentResponse
import com.dev.frequenc.ui_codes.data.models.BillingInformation
import com.dev.frequenc.ui_codes.data.models.paymentInitiateReq
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.Constants
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.view.CardInputWidget
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class StripePaymentActivity : AppCompatActivity() {

    val STRIPE_PUBLISHABLE_KEY="pk_test_51Msg80SDrSIXwtthzwmV8YXpzPbxhk0M1atGmMxtyf0ZlmpA6cWKdRiIZijNimCwD9vLiC21yFkYhhLbvDKG86y800dgr69PR1"
    val STRIPE_SECRET_KEY="sk_test_51Msg80SDrSIXwtthWlqKansXTFnhImdlF6IHkSSJEpzNRdhfRcRB3yW8kDHE8lxGepKYodzrQrrxwKR1JaSTJCJt00r8N3nAaT"
    val STRIPE_ACCOUNT_ID = ""
    
    // we need paymentIntentClientSecret to start transaction
    private var paymentIntentClientSecret: String? = null
    private lateinit var paymentLauncher: PaymentLauncher

    lateinit var progressDialog : ProgressBar
    lateinit var authorization : String
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var payButton : Button
    lateinit var cardInputWidget : CardInputWidget

    lateinit var count : String
    lateinit var eventDetails : EventResponse
    lateinit var item : EventTicket
    lateinit var audiencce :AudienceDataResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stripe_payment)

        payButton = findViewById(R.id.payButton)
        cardInputWidget = findViewById(R.id.cardInputWidget)

        progressDialog = findViewById(R.id.progress_bar)

        eventDetails = intent.getSerializableExtra("eventDetail") as EventResponse

        item = intent.getSerializableExtra("item") as EventTicket

        audiencce = intent.getSerializableExtra("audience") as AudienceDataResponse
        count = intent.getStringExtra("count").toString()


        val paymentConfiguration = PaymentConfiguration.getInstance(applicationContext)
        paymentLauncher = PaymentLauncher.Companion.create(
            this,
            STRIPE_PUBLISHABLE_KEY,
            STRIPE_ACCOUNT_ID,
            this::onPaymentResult
        )

        PaymentConfiguration.init(
            applicationContext,
            STRIPE_PUBLISHABLE_KEY    )

        startCheckout()

    }


    private fun startCheckout() {

        sharedPreferences = getSharedPreferences(Constants.SharedPreference, Context.MODE_PRIVATE)!!

        authorization =  sharedPreferences.getString(Constants.Authorization, "-1").toString()


        var billingInformation : BillingInformation = BillingInformation(
            audiencce.fullName,
            "Singh",
            audiencce.email,
            audiencce.mobile_no,
            "my address",
            "Delhi",
            "Noida",
            "India"
        )


        val billingInformationList = listOf(billingInformation)

        var initiateReq : paymentInitiateReq = paymentInitiateReq(
            item.price * count.toDouble(),
            eventDetails.eventDetails._id,
            count.toInt(),
            item.ticket_type,
            item.price.toDouble(),
            billingInformationList

        )

        ApiClient.getInstance()!!.initiatePayment(authorization,initiateReq)!!.
        enqueue(object : retrofit2.Callback<InitiatePaymentResponse>{


            override fun onResponse(
                call: Call<InitiatePaymentResponse>,
                response: Response<InitiatePaymentResponse>
            ) {

                progressDialog.visibility = View.GONE
                if(response.isSuccessful && response.body()!=null)
                {

                    val res = response.body()

                    Toast.makeText(this@StripePaymentActivity,res!!.stripePayment.clientSecret,
                        Toast.LENGTH_SHORT).show()
                    Log.d("client secret",res!!.stripePayment.clientSecret)

                    paymentIntentClientSecret = res!!.stripePayment.clientSecret

                    payButton.setOnClickListener {
                        cardInputWidget.paymentMethodCreateParams?.let { params ->
                            val confirmParams = ConfirmPaymentIntentParams
                                .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret!!)
                            lifecycleScope.launch {
                                paymentLauncher.confirm(confirmParams)
                            }
                        }
                    }

                }

            }

            override fun onFailure(call: Call<InitiatePaymentResponse>, t: Throwable) {
                progressDialog.visibility = View.GONE

            }
        })

    }


    private fun onPaymentResult(paymentResult: PaymentResult) {
        val message = when (paymentResult) {
            is PaymentResult.Completed -> {
                "Completed!"
            }
            is PaymentResult.Canceled -> {
                "Canceled!"
            }
            is PaymentResult.Failed -> {
                // This string comes from the PaymentIntent's error message.
                // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
                "Failed: " + paymentResult.throwable.message
            }
        }
        Toast.makeText(this,
            "Payment Result:" +
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}


