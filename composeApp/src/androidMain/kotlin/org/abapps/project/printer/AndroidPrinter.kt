package org.abapps.project.printer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import net.posprinter.CPCLPrinter
import net.posprinter.IConnectListener
import net.posprinter.IDeviceConnection
import net.posprinter.POSConnect
import util.Printer


class AndroidPrinter(val context: Context) : Printer {

    var curConnect: IDeviceConnection? = null
    var printer: CPCLPrinter? = null

    init {
        POSConnect.init(this.context)
    }

    override fun connectBtandPrintReceipt(macAddress: String) {
        val connectListener = IConnectListener { code, connInfo, msg ->
            when (code) {
                POSConnect.CONNECT_SUCCESS -> {
                    Toast.makeText(context, "Connect success", Toast.LENGTH_SHORT).show()
                    printer = CPCLPrinter(curConnect)
                    printReceipt()
                }

                POSConnect.CONNECT_FAIL -> {
                    Toast.makeText(
                        context,
                        "Connect failed : ${connInfo} - - $msg",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                POSConnect.CONNECT_INTERRUPT -> {
                    Toast.makeText(context, "connection has disconnected", Toast.LENGTH_SHORT)
                        .show()
                }

                POSConnect.SEND_FAIL -> {
                    Toast.makeText(context, "Send Faileds", Toast.LENGTH_SHORT).show()
                }
            }
        }
        curConnect?.close()
        curConnect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_BLUETOOTH)
        curConnect!!.connect(macAddress, connectListener)
    }

    private fun printReceipt() {
        val items: List<printerItem> = listOf(
            printerItem(),
            printerItem(),
            printerItem(),
        )
        var icon: Bitmap? = BitmapFactory.decodeResource(
            context.resources,
            org.abapps.project.R.drawable.icon
        )

        try {
            val rowHeight = 40 // Adjust based on your font size
            val headerHeight = 40
            val textHeight = 350 // Height for "Abdelwahab" text
            val taxInfoHeight = 60 // Height for "TAX Record No." and "204-890-667"
            val paymentDetailsHeight = 220 // Height for payment details section
            val closingMessageHeight = 360 // Height for closing message section
            val totalHeight =
                textHeight + taxInfoHeight + headerHeight + paymentDetailsHeight + (rowHeight + 10) * items.size + closingMessageHeight // 100 for margins

            printer!!.initializePrinter(totalHeight)
            printer!!.addCGraphics(150, 10, 300, icon)


            // Add "Abdelwahab" centered at the top
            val maxWidth = 600
            val text = "Abdelwahab"
            val textX =
                (maxWidth - text.length * 12) / 2 // Assuming each character is approximately 12 pixels wide
            printer!!.addText(textX, 310, text)

            // Add "TAX Record No." and "204-890-667" centered below "Abdelwahab"
            val taxText = "TAX Record No."
            val taxX = (maxWidth - taxText.length * 12) / 2
            printer!!.addText(taxX, textHeight, taxText)

            val taxNumber = "204-890-667"
            val taxNumX = (maxWidth - taxNumber.length * 12) / 2
            printer!!.addText(taxNumX, textHeight + 20, taxNumber)

            // Add additional text sections
            val dinInText = "DinIn"
            val checkText = "Check: 81993"
            val dateText = "Date: 6/26/2024"
            val closeText = "Close: 12:48 PM"
            val coversText = "Covers : 2"
            val openCloseText = "Open: 2:22 PM"
            val tableCoversText = "Table: 34"
            val cashierText = "Cashier: 2"

            val sectionX = 10
            var currentY = textHeight + taxInfoHeight + 20
            val lineHeight = 30

            val dinInTextX = (maxWidth - taxNumber.length * 12) / 2

            printer!!.addText(dinInTextX, currentY, dinInText)
            currentY += lineHeight
            printer!!.addText(sectionX, currentY, checkText)
            printer!!.addText(350, currentY, dateText)
            currentY += lineHeight
            printer!!.addText(sectionX, currentY, openCloseText)
            printer!!.addText(350, currentY, closeText)
            currentY += lineHeight
            printer!!.addText(sectionX, currentY, tableCoversText)
            printer!!.addText(350, currentY, coversText)
            currentY += lineHeight
            printer!!.addText(sectionX, currentY, cashierText)
            currentY += lineHeight

            // Draw a line after the additional text section
            printer!!.addLine(sectionX, currentY, maxWidth - sectionX, currentY, 2)
            currentY += 20 // Space after the line

            // Add header text for the table below the payment details
            printer!!.addText(sectionX, currentY, "qty")
            printer!!.addText(sectionX + 90, currentY, "item")
            printer!!.addText(sectionX + 500, currentY, "total")

            // Draw lines to create table structure
            // Horizontal line below headers
            currentY += lineHeight
            printer!!.addLine(sectionX, currentY, maxWidth - sectionX, currentY, 2)

            val startYLine = currentY - 20 - lineHeight

            // Add table rows
            currentY += 10 // Space before table rows
            for (item in items) {
                printer!!.addText(sectionX, currentY, item.qty.toString())
                printer!!.addText(sectionX + 100, currentY, item.name)
                printer!!.addText(sectionX + 500, currentY, item.total.toString())
                currentY += rowHeight
                // Draw horizontal line below the row
                //printer!!.addLine(sectionX, currentY, maxWidth - sectionX, currentY, 2)
                currentY += 10 // Space between rows
            }
            printer!!.addLine(sectionX, currentY, maxWidth - sectionX, currentY, 2)
            // Vertical lines for table columns
//            printer!!.addLine(
//                sectionX + 80,
//                startYLine,
//                sectionX + 80,
//                currentY - 10,
//                2
//            )  // Between qty and item
//            printer!!.addLine(
//                sectionX + 480,
//                startYLine,
//                sectionX + 480,
//                currentY - 10,
//                2
//            ) // Between item and total

            // Print subtotal, service charge, VAT, total, and payment details
            val subtotalText = "Sub Total :     30.62"
            val serviceChargeText = "12% service :  3.670"
            val vatText = "14% VAT :       4.800"
            val totalText = "Total              39.09"
            val cashText = "Cash :           39.090"

            currentY += 20 // Space before payment details
            printer!!.addText(sectionX, currentY, subtotalText)
            currentY += lineHeight
            printer!!.addText(sectionX, currentY, serviceChargeText)
            currentY += lineHeight
            printer!!.addText(sectionX, currentY, vatText)
            currentY += lineHeight
            printer!!.addText(sectionX, currentY, totalText)
            currentY += lineHeight
            printer!!.addText(sectionX, currentY, cashText)

            // Draw a line after payment details
            printer!!.addLine(sectionX, currentY + 50, maxWidth - sectionX, currentY + 50, 2)

            // Add closing message
            currentY += 40 // Space before closing message
            val closingText1 = "Thank You"
            val closingText2 = "For Your Visit"

            val closingText1X = (maxWidth - closingText1.length * 12) / 2
            val closingText2X = (maxWidth - closingText2.length * 12) / 2
            val dotX = (maxWidth - ".".length * 12) / 2

            printer!!.addText(dotX, currentY + 50, ".")
            printer!!.addText(dotX, currentY + 80, ".")
            printer!!.addText(dotX, currentY + 110, ".")

            printer!!.addText(closingText1X, currentY + 150, "Thank You")
            printer!!.addText(closingText2X, currentY + 180, "For Your Visit")

            // Print the receipt
            printer!!.addPrint()

        } catch (throwable: Throwable) {
            Log.e("TAG", "printReceipt:$throwable ")
        }
    }


}

data class printerItem(
    val qty: Double = 2.0,
    val name: String = "salad",
    val total: Double = 2.0
)