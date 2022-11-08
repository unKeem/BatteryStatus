package com.example.batterystatus

import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.batterystatus.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    //1. 정적 브로드캐스트 리시버 사용(정보받음)
    //2. 동적 브로드캐스트 리시버 사용(정보받음)
    //3.동적 브로드캐스트 리시버 사용(정보획득) : 충전정보상태값을 가져오겠다.

        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)

        binding.btnReciever.setOnClickListener{
            registerReceiver(null, intentFilter).apply {
                //배터리 상태를 체크
                when(this?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)){
                    //배터리 충전중인지 아닌지 체크
                    BatteryManager.BATTERY_STATUS_CHARGING ->{
                        //usb충전인지 ac충전인지 체크
                        when(getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)){
                            BatteryManager.BATTERY_PLUGGED_AC ->{
                                binding.tvCharging.text = "AC로 충전중"
                                binding.ivCharging.setImageResource(R.drawable.ic_power_)
                            }
                            BatteryManager.BATTERY_PLUGGED_USB ->{
                                binding.tvCharging.text = "USB로 충전중"
                                binding.ivCharging.setImageResource( R.drawable.ic_usb)
                            }
                        }
                    }
                    BatteryManager.BATTERY_STATUS_DISCHARGING ->{
                        binding.tvCharging.text = "충전중이 아님"
                        binding.ivCharging.setImageResource(R.drawable.ic_battery_0)
                    }
                    //예외조건
                    else ->{
                        binding.tvCharging.text = "정보없음"
                        binding.ivCharging.setImageResource(R.drawable.ic_battery_unknown)
                    }
                }

                //충전량 계산
                val level = this?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = this?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val batteryPercent = scale?.let{level?.div(it.toFloat())}
                if(batteryPercent !=null){
                    val df1 = DecimalFormat("##00.0")
                    binding.tvChargingValue.text = "${df1.format(batteryPercent*100)}%"
                }

            }//registerReceiver

            val intent = Intent(this, MyReceiver::class.java)
            intent.putExtra("batteryPercent", binding.tvChargingValue.text.toString())
            sendBroadcast(intent)


        }//btnReciever exit

    }
}