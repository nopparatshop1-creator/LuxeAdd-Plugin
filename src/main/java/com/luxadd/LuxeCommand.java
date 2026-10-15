// คำสั่งกดดาวน์โหลด Resource Pack
if (args.length > 0 && args[0].equalsIgnoreCase("pack")) {
    if (sender instanceof Player player) {
        String url = config.getString("resource-pack.url", "");
        if (url.isEmpty() || url.contains("example.com")) {
            player.sendMessage(ChatColor.RED + "Resource pack URL is not properly configured!");
            return true;
        }
        
        String prompt = ChatColor.translateAlternateColorCodes('&', 
                config.getString("resource-pack.prompt-message", "&eDo you want to download the server resource pack?"));
        boolean force = config.getBoolean("resource-pack.force", false);
        
        try {
            // ส่งเฉพาะ URL, Prompt และ Force (ตรงตาม Paper API 1.21+)
            player.setResourcePack(url, prompt, force);
            player.sendMessage(ChatColor.GREEN + "Sending resource pack request...");
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error sending resource pack: " + e.getMessage());
        }
    } else {
        sender.sendMessage("This command can only be used by players.");
    }
    return true;
}
