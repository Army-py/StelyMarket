package fr.army.stelymarket.utils.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.bukkit.block.BlockState;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;


public class BlockSerializer {

    public byte[] serializeToByte(BlockState blockState) {
        try {
            final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

            final BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(blockState);
            objectOutputStream.flush();


            return arrayOutputStream.toByteArray();
        } catch (final Exception exception) {
            throw new RuntimeException("Error turning BlockState into byte", exception);
        }
    }


    public BlockState deserializeFromByte(byte[] bytes) {
        try {
            final ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
            final BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(arrayInputStream);
            return (BlockState) objectInputStream.readObject();
        } catch (final Exception exception) {
            throw new RuntimeException("Error turning byte into BlockState", exception);
        }
    }
}
