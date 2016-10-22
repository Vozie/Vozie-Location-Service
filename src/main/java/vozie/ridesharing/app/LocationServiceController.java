package vozie.ridesharing.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationServiceController {

    @RequestMapping("/locationservice")
    public String locationService(@RequestParam(value="command", required=false, defaultValue="") String command,
    		@RequestParam(value="arg1", required=false, defaultValue="") String arg1,
    		@RequestParam(value="arg2", required=false, defaultValue="") String arg2,
    		@RequestParam(value="arg3", required=false, defaultValue="") String arg3,
    		@RequestParam(value="arg4", required=false, defaultValue="") String arg4) {
    		LocationService locationService = new LocationService(command, arg1, arg2, arg3, arg4);
        return locationService.getContent();
    }
}
