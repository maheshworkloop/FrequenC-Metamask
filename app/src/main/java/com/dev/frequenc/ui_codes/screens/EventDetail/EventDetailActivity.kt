package com.dev.frequenc.ui_codes.screens.EventDetail

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.frequenc.R
import com.dev.frequenc.databinding.ActivityEventDetailBinding
import com.dev.frequenc.ui_codes.MainActivity
import com.dev.frequenc.ui_codes.data.AllDataResponse
import com.dev.frequenc.ui_codes.screens.Adapter.ArtistAdapter
import com.dev.frequenc.ui_codes.screens.Adapter.SliderAdapter
import com.dev.frequenc.ui_codes.screens.Adapter.TicketAdapter
import com.dev.frequenc.ui_codes.screens.Adapter.VenueAdapter
import com.dev.frequenc.ui_codes.screens.ArtistDetail.ArtistDetailsActivity
import com.dev.frequenc.ui_codes.screens.VenueDetail.VenueDetailActivity
import com.dev.frequenc.ui_codes.data.Artist
import com.dev.frequenc.ui_codes.data.EventResponse
import com.dev.frequenc.ui_codes.data.EventTicket
import com.dev.frequenc.ui_codes.data.TrendingEventsResponse
import com.dev.frequenc.ui_codes.data.VenueDetails
import com.dev.frequenc.ui_codes.screens.Adapter.TrendingEventAdapter
import com.dev.frequenc.ui_codes.screens.SelectTicket.SelectTicketActivity
import com.dev.frequenc.ui_codes.screens.ViewAllTrendingEvents.ViewAllTrendingEvents
import com.dev.frequenc.ui_codes.screens.utils.ApiClient
import com.dev.frequenc.util.AppCommonMethods
import com.dev.frequenc.ui_codes.util.Constants
//import com.smarteist.autoimageslider.SliderView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EventDetailActivity : AppCompatActivity(), TicketAdapter.ListAdapterListener,
    ArtistAdapter.ListAdapterListener, VenueAdapter.ListAdapterListener,TrendingEventAdapter.ListAdapterListener { // End of Program

    private var selectedDate: String? = null
    private var selectedItem: EventTicket? = null
    private var selectedList: List<EventTicket>? = null

    //    lateinit var progressDialog : ProgressBar
    lateinit var id: String
    var str = ""
    private lateinit var binding: ActivityEventDetailBinding
    lateinit var eventResponse: EventResponse

    var flagFaq : Boolean = false
    var flagTerms : Boolean = false
    lateinit var trendingEventList : List<TrendingEventsResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBackBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
//        startActivity(Intent(this, MainActivity::class.java))
//            finishAffinity()
        }

        trendingEventApi()


//        progressDialog = findViewById(R.id.progress_bar2)

        val receivedIntent = intent
        val receivedAction = receivedIntent.action
        val receivedType = receivedIntent.type

        if (Intent.ACTION_SEND == receivedAction && receivedType == "text/plain") {
            val receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT)
            if (receivedText != null) {
                // You have received a text message, do something with it.
                // For example, display it in a TextView.
                str = receivedText

//                Toast.makeText(this,str,Toast.LENGTH_SHORT).show()

                id = str.substringAfter("ID-")

                eventDetailApi(id)

            }
        } else {
            val bundle = intent.extras
            if (bundle != null) {
                id = bundle.getString("eventid").toString()
                Log.d("eventid", id)
                eventDetailApi(id)
            }

        }

        binding.cvFaq.setOnClickListener {

            flagFaq = !flagFaq

            if(flagFaq)
                binding.tvFaqIcon.setImageDrawable(getDrawable(R.drawable.minus))
            else
                binding.tvFaqIcon.setImageDrawable(getDrawable(R.drawable.plus))

        }

        binding.cvTerms.setOnClickListener {

            flagTerms = !flagTerms

            if(flagTerms)
                binding.tvTermsConditionIcon.setImageDrawable(getDrawable(R.drawable.minus))
            else
                binding.tvTermsConditionIcon.setImageDrawable(getDrawable(R.drawable.plus))

        }

        binding.btnBuy.setOnClickListener {
            if (selectedItem == null || selectedList.isNullOrEmpty()) {
                Toast.makeText(applicationContext, "Select Any Ticket Type ", Toast.LENGTH_SHORT)
                    .show()
//            } else if (selectedDate == null || selectedDate!!.length > 8) {
//                Toast.makeText(applicationContext, "Select Any Date ", Toast.LENGTH_SHORT)
//                    .show()
            } else {
                val intent = Intent(this, SelectTicketActivity::class.java)
                intent.putExtra("item", selectedItem as Serializable)
                intent.putExtra("list", selectedList as Serializable)
                intent.putExtra("eventDetail", eventResponse as Serializable)
//                intent.putExtra("date", selectedDate as String)
                startActivity(intent)
            }
        }
        binding.ivCalendar.setOnClickListener {
//            dateTimePickerDialog()
        }

    } //onCreate


    private fun eventDetailApi(id: String) {
        binding.progressBar2.visibility = View.VISIBLE

        Log.d("eventId3", id)


        ApiClient.getInstance()!!.getEvent(id)!!
            .enqueue(object : retrofit2.Callback<List<EventResponse>> {
                override fun onResponse(
                    call: Call<List<EventResponse>>,
                    response: retrofit2.Response<List<EventResponse>>
                ) {

                    binding.progressBar2.visibility = View.GONE

                    Log.d(Constants.ApiResponse, "on Api Hit EventDetail: " + response.body())

                    if (response.isSuccessful) {
                        val eventResponses = response.body()
                        if (eventResponses != null && eventResponses.isNotEmpty()) {

                            eventResponse =
                                eventResponses[0] // Assuming you expect one object in the list

                            Log.d(
                                Constants.ApiResponse,
                                "on Api Hit EventDetail Success: " + eventResponse.eventDetails.eventTitle
                            )

                            // Access the data as needed, for example:
                            val eventTitle = eventResponse.eventDetails.eventTitle
                            val artistName = eventResponse.artist[0].artist_name


                            binding.tvDescription.text = eventResponse.eventDetails.description

                            binding.tvEventName.text = eventResponse.eventDetails.eventTitle

                            Log.d("linecount2", "linecount ${binding.tvDescription.lineCount}")

                            binding.tvOnwards.text = "FRQ " + eventResponse.startPrice.toInt().toString()

                            binding.tvOnwardsTag.visibility = View.VISIBLE

                            binding.tvLocation.text = eventResponse.venueDetails.venue_locality

                            var date = eventResponse.eventDetails.eventStartDate

                            binding.tvDate.text = AppCommonMethods.convertDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd MMM yyyy", date
                            )

                            binding.tvTime.text = AppCommonMethods.convertDateFormat2(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "HH:mm:ss", date
                            )

                            if (binding.tvDescription.lineCount.toInt() > 3) {
                                Log.d("linecount", "linecount ${binding.tvDescription.lineCount}")

                                binding.tvReadMore.visibility = View.VISIBLE
                                binding.tvReadLess.visibility = View.GONE
                                binding.tvDescription.maxLines = 3

                                binding.tvDescription.text = eventResponse.eventDetails.description



                                binding.tvReadMore.setOnClickListener {
                                    binding.tvReadMore.visibility = View.GONE
                                    binding.tvReadLess.visibility = View.VISIBLE
                                    binding.tvDescription.maxLines = Int.MAX_VALUE

                                    binding.tvDescription.text =
                                        eventResponse.eventDetails.description

                                }

                                binding.tvReadLess.setOnClickListener {
                                    binding.tvReadMore.visibility = View.VISIBLE
                                    binding.tvReadLess.visibility = View.GONE
                                    binding.tvDescription.maxLines = 3
                                    binding.tvDescription.text =
                                        eventResponse.eventDetails.description

                                }

                            }

                            binding.rvTicket.apply {
                                layoutManager = LinearLayoutManager(
                                    this@EventDetailActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter = TicketAdapter(
                                    eventResponse.eventTicket,
                                    this@EventDetailActivity
                                )
                            }

                            binding.rvArtist.apply {
                                layoutManager = LinearLayoutManager(
                                    this@EventDetailActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter =
                                    ArtistAdapter(eventResponse.artist, this@EventDetailActivity)
                            }


                            binding.rvVenue.apply {
                                layoutManager = LinearLayoutManager(
                                    this@EventDetailActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter = VenueAdapter(
                                    eventResponse.venueDetails,
                                    this@EventDetailActivity
                                )
                            }


                            binding.rlShare.setOnClickListener {
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "text/plain"
                                shareIntent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Welcome to the event  ${eventResponse.eventDetails.eventTitle} at  ${eventResponse.venueDetails.venue_locality} Event ID-${id}"
                                )
//                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, eventResponse.eventDetails.eventTitle)
                                startActivity(Intent.createChooser(shareIntent, "Share..."))
                            }
//
                            try {
                                val adapter = SliderAdapter(
                                    eventResponse.eventDetails.eventImage,
                                    this@EventDetailActivity
                                )
                                binding.sliderView.setSliderAdapter(adapter)
                                binding.sliderView.scrollTimeInSec = 3
                                binding.sliderView.isAutoCycle = true
                                binding.sliderView.startAutoCycle()
                            } catch (e: Exception) {

                            }
                            // Access other fields as required
                        }
                    } else {
                        // Handle the error
                    }
                }

                override fun onFailure(call: Call<List<EventResponse>>, t: Throwable) {
                    binding.progressBar2.visibility = View.GONE

                    // Handle network or other errors
                }
            })


        /*
        ApiClient.getInstance()!!.getEvent(id)!!.enqueue(object :
            Callback<List<EventDetailResponse>> {
            override fun onResponse(
                call: Call<List<EventDetailResponse>>,
                response: Response<List<EventDetailResponse>>
            ) {

                Log.d(Constants.ApiResponse, "on Api Hit EventDetail: " + response.body() )

                if (response.isSuccessful() && response.body() != null
                ) {
                    Log.d(Constants.ApiResponse, "onResponse Retrofit: " + response.body())


                    val eventResponses = response.body()

                    if (eventResponses != null && eventResponses.isNotEmpty()) {
                        val eventResponse = eventResponses[0] // Assuming you expect one object in the list
                        // Access the data as needed, for example:
                        val eventTitle = eventResponse.eventDetails.eventTitle
                        val artistName = eventResponse.artist[0].artist_name

                        Log.d("eventTitle",eventTitle)
                        Log.d("artistName",artistName)

                        // Access other fields as required
                    }
                } else {
                    // Handle the error
                }
                }



            override fun onFailure(call: Call<List<EventDetailResponse>>, t: Throwable) {
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)
//                progressDialog.visibility = View.GONE
            }


        })

            */

    }     // End of Event Detail Api

    private fun trendingEventApi() {
//        binding.progressBar2.visibility = View.VISIBLE

        ApiClient.getInstance()!!.trendingEvents()!!.enqueue(object :
            Callback<List<TrendingEventsResponse>> {
            override fun onResponse(
                call: Call<List<TrendingEventsResponse>>,
                response: Response<List<TrendingEventsResponse>>
            ) {


//                binding.progressBar2.visibility = View.GONE

                if (response.isSuccessful() && response.body() != null
                ) {

//                    rlTrendingEvent.visibility = View.VISIBLE

                    Log.d(Constants.ApiResponse, "onResponse Retrofit: " + response.body())

                    val res = response.body()

                    trendingEventList = res!!

                    binding.tvTrendingEventsViewAll.setOnClickListener {
                        val intent = Intent(this@EventDetailActivity, ViewAllTrendingEvents::class.java)
                        intent.putExtra("list",trendingEventList as Serializable)
                        startActivity(intent)
                    }

                    for(i in res!!.indices)
                    {
                        Log.d(Constants.ApiResponse, " Body : ${res[i]!!.eventTitle}")

                    }

                    binding.rvTrendingEvents.apply {
                        layoutManager = LinearLayoutManager(this@EventDetailActivity,LinearLayoutManager.HORIZONTAL,false)
                        adapter = TrendingEventAdapter(res,this@EventDetailActivity,false)
                    }


                } else {
//                    genericTypeResponse.postValue(null)
                    //                        if (response.code() )\
                }
            }

            override fun onFailure(call: Call<List<TrendingEventsResponse>>, t: Throwable) {
//                setIsLoading(false)
                Log.e(Constants.ApiError, "onFailure Retrofit: ", t)
//                progressDialog.visibility = View.GONE

            }
        })

    }

    private fun dateTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a date picker dialog
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Handle the selected date
                this.selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDay)
                    }.time)
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        // Show the date picker dialog
        datePickerDialog.show()

    }

    override fun onClickAtTicket(item: EventTicket, mList: List<EventTicket>) {
        this.selectedItem = item
        this.selectedList = mList
    }

    override fun onClickAtArtist(item: Artist) {

        val intent = Intent(this, ArtistDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putString("artist_id", item._id)
        Log.d("artist_id", item._id)
        intent.putExtras(bundle)
        startActivity(intent)


    }

    override fun onClickAtVenue(item: VenueDetails) {

        val intent = Intent(this, VenueDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString("venue_id", item._id)
        Log.d("venue_id", item._id)
        intent.putExtras(bundle)
        startActivity(intent)

    }



    override fun onClickAtCard(item: TrendingEventsResponse) {
        val intent = Intent(this,EventDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString("eventid",item._id)
        Log.d("eventid",item._id)

        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onClickAtBookmark(item: TrendingEventsResponse) {
    }


}