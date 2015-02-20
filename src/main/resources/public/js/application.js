/**
 * Created by cepe on 20.02.2015.
 */
function initSelect(name, maxItems) {
    if (name) {
        
        if (!maxItems) { 
            maxItems = 2;
        }
        
        $(name).selectize({
            maxItems: maxItems

        });
    }
};
