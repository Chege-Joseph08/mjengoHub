import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    ProcessBuilder process = new ProcessBuilder();
     Integer port;
     if (process.environment().get("PORT") != null) {
         port = Integer.parseInt(process.environment().get("PORT"));
     } else {
         port = 4567;
     }

    setPort(port);

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Items
    get("/items", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      // model.put("items", Item.all());
      model.put("template", "templates/items.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Portal
    get("/login", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/seller_portal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Saving new Sellers
    post("/sellers", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String password = request.queryParams("password");
      String email = request.queryParams("email");
      String phone = request.queryParams("phone");
      Seller newSeller = new Seller(name, password, email, phone);
      newSeller.save();
      model.put("fontColor", "green");
      model.put("msg", "Seller Added Successfuly!!");
      model.put("template", "templates/success.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      // Updating Sellers
      post("/seller", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        int sellerId = Integer.parseInt(request.queryParams("sellerid"));
        Seller seller = Seller.find(sellerId);
        String name = request.queryParams("name").toUpperCase();
        String password = request.queryParams("password");
        String email = request.queryParams("email");
        String phone = request.queryParams("phone");
        seller.update(name, password, email, phone);
        seller = Seller.find(sellerId);
        model.put("seller", seller);
        model.put("template", "templates/sellerpage.vtl");

        return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

      // User login
      post("/login", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        String name = request.queryParams("name").toUpperCase();
        String password = request.queryParams("password");
        // Gen gen = new Gen();
        int found = Gen.SellerChecker(name,password);

        if(found == 0){
          //Wrong username & Password
          model.put("fontColor", "red");
          model.put("msg", "Wrong Password");
          model.put("template", "templates/success.vtl");
        }else{
          Seller seller = Seller.login(name, password);
          model.put("fontColor", "green");
          model.put("seller", seller);
          model.put("template", "templates/sellerpage.vtl");
        }
        return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/:id/newitem", (request, response) -> {
          Map<String, Object> model = new HashMap<String, Object>();
          Seller seller = Seller.find(Integer.parseInt(request.params(":id")));
          model.put("seller", seller);
          model.put("template", "templates/new_item_Form.vtl");

          return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        // Saving new Item
        post("/newitem", (request, response) -> {
          Map<String, Object> model = new HashMap<String, Object>();
          String name = request.queryParams("name");
          String location = request.queryParams("location");
          String cost = request.queryParams("cost");
          // String image = request.queryParams("image");
          String image = "jpg";
          int sellerId = Integer.parseInt(request.queryParams("sellerid"));
          Item newItem = new Item(name, location, cost, image, sellerId);
          newItem.save();
          Seller seller = Seller.find(sellerId);
          model.put("seller", seller);
          model.put("template", "templates/sellerpage.vtl");

          return new ModelAndView(model, layout);
          }, new VelocityTemplateEngine());

          get("/:id/edit", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Seller seller = Seller.find(Integer.parseInt(request.params(":id")));
            model.put("seller", seller);
            model.put("template", "templates/edit_seller.vtl");

            return new ModelAndView(model, layout);
          }, new VelocityTemplateEngine());

          // Canceling SelletEdit
          post("/seller", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            int sellerId = Integer.parseInt(request.queryParams("sellerid"));
            Seller seller = Seller.find(sellerId);
            model.put("seller", seller);
            model.put("template", "templates/sellerpage.vtl");

            return new ModelAndView(model, layout);
            }, new VelocityTemplateEngine());






  }
}
