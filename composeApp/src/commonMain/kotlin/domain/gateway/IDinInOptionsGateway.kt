package domain.gateway


interface IDinInOptionsGateway {
    suspend fun enableTable(tableId: Int, checkId: Long) 
    suspend fun disableTable(tableId: Int, checkId: Long) 
    suspend fun splitCheck(tableId: Int, checkId: Long) 
    suspend fun unSplitCheck(tableId: Int, checkId: Long) 
    suspend fun unCombineCheck(tableId: Int, checkId: Long) 
    suspend fun void(tableId: Int, checkId: Long) 
    suspend fun splitAndPay(tableId: Int, checkId: Long) 
    suspend fun shareItem(tableId: Int, checkId: Long) 
    suspend fun moveItemToNewCheck(tableId: Int, checkId: Long) 
    suspend fun moveTableChecks(selectedTablesIds: List<Int>, checkId: Long) 
    suspend fun combineCheck(selectedTablesIds: List<Int>, checkId: Long) 
    suspend fun moveItem(selectedTablesIds: List<Int>, checkId: Long) 
}