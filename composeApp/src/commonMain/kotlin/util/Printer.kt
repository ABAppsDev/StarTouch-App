package util

import PlatformContext

interface Printer {
    fun connectBtandPrintReceipt(macAddress: String)
}

expect fun createPrinter(context: PlatformContext): Printer

expect fun browseBluetoothDevices(context: PlatformContext, onSelectDevice: (String?) -> Unit)
