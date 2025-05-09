package fr.xyness.SCS.Guis.Bedrock;

import fr.xyness.SCS.Zone;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import fr.xyness.SCS.SimpleClaimSystem;
import fr.xyness.SCS.Commands.ClaimCommand;

/**
 * Bedrock confirmation GUI for adding Chunk/Zone to claim.
 */
public class BChunkConfirmationGui {

	
    // ***************
    // *  Variables  *
    // ***************

    
    /** Floodgate Player */
    private final FloodgatePlayer floodgatePlayer;

    
    // ******************
    // *  Constructors  *
    // ******************

    
    /**
     * Main constructor for the BChunkConfirmationGui.
     *
     * @param player The player for whom the GUI is being created.
     * @param instance The instance of the SimpleClaimSystem plugin.
     * @param price The price.
     */
    public BChunkConfirmationGui(Player player, SimpleClaimSystem instance, double price, Zone zone) {
    	this.floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
    	if (zone != null) price = 0.0;  // Zones can't be purchased (They must be added to an existing Claim).
    	String lore = "";
    	if(instance.getSettings().getBooleanSetting("economy") && price > 0) {
    		lore += instance.getLanguage().getMessage("bedrock-chunk-confirm-info-lore-economy", null)
    				.replace("%price%", instance.getMain().getPrice(String.valueOf(price)))
    				.replace("%money-symbol%", instance.getLanguage().getMessage("money-symbol", null))+"\n";
    	}
		// zone: null since these questions and captions are generic.
    	lore += instance.getLanguage().getMessage("bedrock-chunk-confirm-info-lore", null);
    	
        // Création d'un formulaire simple
    	ModalForm form = ModalForm.builder()
	        .title(instance.getLanguage().getMessage("bedrock-gui-chunk-confirm-title", null))  // "Confirm adding chunk?"
	        .content(lore)
	        .button1(instance.getLanguage().getMessage("bedrock-confirm-title", null))
	        .button2(instance.getLanguage().getMessage("bedrock-cancel-title", null))
	        .invalidResultHandler(() -> ClaimCommand.isOnAdd.remove(player))
	        .validResultHandler(response -> {
	        	int clickedSlot = response.clickedButtonId();
	        	if(clickedSlot == 0) {
	            	String claimName = ClaimCommand.isOnAdd.get(player);
	            	Bukkit.dispatchCommand(player, "claim addchunk "+claimName);
	            	return;
	        	}
	        	ClaimCommand.isOnAdd.remove(player);
	        	return;
	        })
	        .build();
        
        floodgatePlayer.sendForm(form);
    }

}
