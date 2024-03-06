define(["jquery", "bootstrap/modal"], function($, modal) {

    let activate = function(modalId) {
    console.log("2. Callback function builder, "+modalId);
        $(modalId).modal('show');
        
        console.log("Activate method executed. Length:"+$(modalId).length);
        console.log("Element is "+$(modalId)+", "+modalId);
        
        $(modalId).on('hide.bs.modal', function () {
            console.log("Created event handler for hidden.bs.modal event");
            $('.modal-backdrop').remove();
            return true;
        });
    }
    
    let hide = function(modalId) {
        console.log("Hide method is executing.");
        console.log("Hiding Element ("+modalId+") - "+$(modalId)+", length: "+$(modalId).length);
 
        if ($(modalId).length > 0) {
            // Hide will trigger removal
            $(modalId).modal('hide');
        }
    }
    
    
    return {
        activate: activate,
        hide: hide
    }
});
