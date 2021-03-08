package io.zen.niu.chat;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "")
public class IndexController {

  @GetMapping(path = { "", "/", "/index", "index.html" },  produces = MediaType.TEXT_HTML_VALUE)
  public String getIndex() {
    return "index";
  }

}
