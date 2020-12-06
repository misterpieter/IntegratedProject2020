package be.volders.integratedproject2020.Signature

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Database

object Student : Table() {
    val student_snr: Column<String> = varchar("student_id",20).uniqueIndex()
    val firstname: Column<String> = varchar("firstname", 20)
    val lastname: Column<String> = varchar("lastname", 20)
    override val primaryKey = PrimaryKey(student_snr, name="PK_STUDENT_ID")
}
object Signature : Table() {
    val student_snr: Column<String> = varchar("student_id",20).uniqueIndex()
    val firstname: Column<String> = varchar("firstname", 20)
    val lastname: Column<String> = varchar("lastname", 20)
    override val primaryKey = PrimaryKey(student_snr, name="PK_STUDENT_ID")
}
object Location : Table() {
    val student_snr: Column<String> = varchar("student_id",20).uniqueIndex()
    val firstname: Column<String> = varchar("firstname", 20)
    val lastname: Column<String> = varchar("lastname", 20)
    override val primaryKey = PrimaryKey(student_snr, name="PK_STUDENT_ID")
}
fun DatabaseKtor() {
    Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
    transaction{
        SchemaUtils.create(Student)


    }

}