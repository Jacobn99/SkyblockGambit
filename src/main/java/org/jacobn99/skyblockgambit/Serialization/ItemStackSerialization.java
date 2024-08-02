package org.jacobn99.skyblockgambit.Serialization;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemStackSerialization implements DeserializeMethod, SerializeMethod {
    public Object Deserialize(String encodedItem) {
        byte[] decodedItem;
        try {
            decodedItem = Base64.getDecoder().decode(encodedItem);
            ByteArrayInputStream in = new ByteArrayInputStream(decodedItem);
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);

            ItemStack newItem = (ItemStack) is.readObject();
            return newItem;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public String Serialize(Object object) {
        String encodedItem;
        ItemStack item = (ItemStack) object;
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

            os.writeObject(item);
            os.flush(); //transfers data from buffer in the os ByteArrayOutputStream object to the io ByteArrayOutputStream object

            byte[] itemSerialized = io.toByteArray();

            encodedItem = Base64.getEncoder().encodeToString(itemSerialized);

            return encodedItem;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
