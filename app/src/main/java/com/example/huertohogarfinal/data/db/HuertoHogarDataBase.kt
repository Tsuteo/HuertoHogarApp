package com.example.huertohogarfinal.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.huertohogarfinal.data.dao.CarritoDao
import com.example.huertohogarfinal.data.dao.ProductoDao
import com.example.huertohogarfinal.data.dao.UsuarioDao
import com.example.huertohogarfinal.data.entities.ItemCarrito
import com.example.huertohogarfinal.data.entities.Producto
import com.example.huertohogarfinal.data.entities.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Usuario::class, Producto::class, ItemCarrito::class],
    version = 3,
    exportSchema = false
)
abstract class HuertoHogarDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile
        private var INSTANCE: HuertoHogarDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): HuertoHogarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HuertoHogarDatabase::class.java,
                    "huerto_hogar_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(HuertoDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class HuertoDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.productoDao(), database.usuarioDao())
                }
            }
        }

        suspend fun populateDatabase(productoDao: ProductoDao, usuarioDao: UsuarioDao) {

            usuarioDao.insertar(
                Usuario(
                    id = 0,
                    nombre = "Admin",
                    apellido = "General",
                    correo = "admin@huerto.cl",
                    contrasena = "admin123",
                    direccion = "Casa Matriz",
                    rol = "ADMIN"
                )
            )

            usuarioDao.insertar(
                Usuario(
                    id = 0,
                    nombre = "Pedro",
                    apellido = "Vendedor",
                    correo = "vendedor@huerto.cl",
                    contrasena = "vende123",
                    direccion = "Sucursal Norte",
                    rol = "EMPLEADO"
                )
            )



            productoDao.insertAll(
                Producto(0, "FR001", "Manzanas Fuji", 1200, 150, "Manzanas Fuji crujientes...", "Frutas Frescas", "Valle del Maule", "ic_manzana"),
                Producto(0, "FR002", "Naranjas Valencia", 1000, 200, "Jugosas y ricas...", "Frutas Frescas", "Zona Central", "ic_naranja"),
                Producto(0, "FR003", "Plátanos Cavendish", 800, 250, "Plátanos maduros...", "Frutas Frescas", "Ecuador/Chile", "ic_platano"),
                Producto(0, "VR001", "Zanahorias Orgánicas", 900, 100, "Zanahorias crujientes...", "Verduras Orgánicas", "Región de O'Higgins", "ic_zanahoria"),
                Producto(0, "VR002", "Espinacas Frescas", 700, 80, "Espinacas frescas...", "Verduras Orgánicas", "Zona Sur", "ic_espinaca"),
                Producto(0, "PO001", "Miel Orgánica", 5000, 50, "Miel pura...", "Productos Orgánicos", "Sur de Chile", "ic_miel")
            )
        }
    }
}