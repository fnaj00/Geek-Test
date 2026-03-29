import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BookDetailsApi {

    private static final Map<String, Book> books = new HashMap<>();
    private static final String UPLOAD_DIR = "images"; // Folder for uploaded images

    private static class Book {
        String id;
        String title;
        String author;
        int publicationYear;
        String isbn;
        String description;
        String coverImageUrl;
        double priceAmount;
        String currency = "USD";
    }

    public static void main(String[] args) throws Exception {
        // Create upload directory if it doesn't exist
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Add a sample book on startup
        Book sample = new Book();
        sample.id = "978-3-16-148410-0";
        sample.title = "Clean Code";
        sample.author = "Robert C. Martin";
        sample.publicationYear = 2008;
        sample.isbn = "978-3-16-148410-0";
        sample.description = "A handbook of agile software craftsmanship...";
        sample.coverImageUrl = "https://via.placeholder.com/300x450?text=Clean+Code";
        sample.priceAmount = 39.99;
        books.put(sample.id, sample);

        // Start the HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/books/", new BookHandler());
        server.createContext("/images/", new ImageHandler()); // Serve images from local dir
        server.createContext("/", new RedirectHandler()); // Redirect root to /books/
        server.setExecutor(null);
        server.start();

        System.out.println("=== Book Details API Server ===");
        System.out.println("Open in your browser: http://localhost:8000/books/");
        System.out.println("This shows a nice HTML list of all books.");
        System.out.println("Click a book → detailed view (same site, with Back button).");
        System.out.println("For raw JSON API: http://localhost:8000/books/<id>?format=json");
        System.out.println();
        System.out.println("Console commands:");
        System.out.println("  add   - Add a new book interactively");
        System.out.println("  list  - List all book IDs");
        System.out.println("  exit  - Stop the server and quit");
        System.out.println();

        // Interactive console for adding books
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            if (command.equals("add")) {
                addBookInteractively(scanner);
            } else if (command.equals("list")) {
                System.out.println("Stored book IDs: " + (books.isEmpty() ? "none" : String.join(", ", books.keySet())));
            } else if (command.equals("exit")) {
                System.out.println("Stopping server...");
                server.stop(0);
                break;
            } else if (!command.isEmpty()) {
                System.out.println("Unknown command. Use: add, list, exit");
            }
        }
        scanner.close();
    }

    private static void addBookInteractively(Scanner scanner) {
        try {
            System.out.println("Enter book details (required fields):");

            System.out.print("ID (unique identifier, e.g. ISBN or slug - spaces will be replaced with -): ");
            String originalId = scanner.nextLine().trim();
            if (originalId.isEmpty()) {
                System.out.println("ID is required.");
                return;
            }
            String id = originalId.replaceAll("\\s+", "-");

            System.out.print("Title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Author: ");
            String author = scanner.nextLine().trim();

            int year = 0;
            while (true) {
                System.out.print("Publication Year (e.g. 2008): ");
                String yearStr = scanner.nextLine().trim();
                if (yearStr.isEmpty()) {
                    System.out.println("Year is required. Please enter a number.");
                    continue;
                }
                try {
                    year = Integer.parseInt(yearStr);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid year: " + e.getMessage() + ". Please enter a number.");
                }
            }

            System.out.print("ISBN: ");
            String isbn = scanner.nextLine().trim();

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Cover Image (local path or URL, or leave empty for placeholder): ");
            String coverInput = scanner.nextLine().trim();
            String coverImageUrl;
            if (coverInput.isEmpty()) {
                coverImageUrl = "https://via.placeholder.com/300x450?text=No+Cover";
            } else if (coverInput.startsWith("http://") || coverInput.startsWith("https://")) {
                coverImageUrl = coverInput; // Use URL directly
            } else {
                // Handle local file upload
                Path sourcePath = Paths.get(coverInput);
                if (!Files.exists(sourcePath)) {
                    System.out.println("File not found: " + coverInput);
                    return;
                }
                String fileName = sourcePath.getFileName().toString();
                Path destPath = Paths.get(UPLOAD_DIR, fileName);
                Files.copy(sourcePath, destPath); // Copy to images dir
                coverImageUrl = "/images/" + URLEncoder.encode(fileName, "UTF-8"); // Serve via server
                System.out.println("Image uploaded successfully!");
            }

            double price = 0.0;
            while (true) {
                System.out.print("Price Amount (e.g. 39.99): ");
                String priceStr = scanner.nextLine().trim();
                if (priceStr.isEmpty()) {
                    System.out.println("Price is required. Please enter a number.");
                    continue;
                }
                try {
                    price = Double.parseDouble(priceStr);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price: " + e.getMessage() + ". Please enter a valid number (e.g., 39.99).");
                }
            }

            Book book = new Book();
            book.id = id;
            book.title = title;
            book.author = author;
            book.publicationYear = year;
            book.isbn = isbn;
            book.description = description;
            book.coverImageUrl = coverImageUrl;
            book.priceAmount = price;

            books.put(id, book);
            System.out.println("Book added successfully!");
            System.out.println("→ Refresh the list page: http://localhost:8000/books/");
            System.out.println("→ Direct details: http://localhost:8000/books/" + URLEncoder.encode(id, "UTF-8"));

        } catch (Exception e) {
            System.out.println("Invalid input - book not added. Error: " + e.getMessage());
        }
    }

    private static class RedirectHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                exchange.getResponseHeaders().add("Location", "/books/");
                exchange.sendResponseHeaders(301, -1);
            } catch (Exception ignored) {}
            exchange.close();
        }
    }

    private static class ImageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String fileName = path.substring("/images/".length());

            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            if (!Files.exists(filePath)) {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
                return;
            }

            String mimeType = Files.probeContentType(filePath);
            if (mimeType == null) mimeType = "application/octet-stream";

            exchange.getResponseHeaders().add("Content-Type", mimeType);
            byte[] bytes = Files.readAllBytes(filePath);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            exchange.close();
        }
    }

    private static class BookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            try {
                if (!"GET".equals(exchange.getRequestMethod())) {
                    sendHtml(exchange, "<h1>405 Method Not Allowed</h1>", 405);
                    return;
                }

                String path = exchange.getRequestURI().getPath();
                String query = exchange.getRequestURI().getQuery();
                boolean wantsJson = query != null && query.contains("format=json");

                String bookId = path.substring("/books/".length());

                if (bookId.isEmpty() || bookId.endsWith("/")) {
                    // List all books
                    String html = buildListHtml();
                    sendHtml(exchange, html, 200);
                    return;
                }

                Book book = books.get(bookId);
                if (book == null) {
                    sendHtml(exchange, buildNotFoundHtml(), 404);
                    return;
                }

                if (wantsJson) {
                    String json = buildJson(book);
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    byte[] response = json.getBytes("UTF-8");
                    exchange.sendResponseHeaders(200, response.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response);
                    }
                } else {
                    String html = buildDetailsHtml(book);
                    sendHtml(exchange, html, 200);
                }
            } catch (Exception e) {
                try { sendHtml(exchange, "<h1>500 Internal Server Error</h1>", 500); } catch (Exception ignored) {}
            } finally {
                exchange.close();
            }
        }
    }

    private static void sendHtml(HttpExchange exchange, String html, int status) throws Exception {
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        byte[] response = html.getBytes("UTF-8");
        exchange.sendResponseHeaders(status, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    private static String buildListHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Book List</title>");
        sb.append("<style>body{font-family:sans-serif;margin:40px;}table{width:100%;border-collapse:collapse;}th,td{border:1px solid #ddd;padding:12px;text-align:left;}th{background:#f0f0f0;}img{height:80px;}</style>");
        sb.append("</head><body>");
        sb.append("<h1>Available Books</h1>");

        if (books.isEmpty()) {
            sb.append("<p>No books yet. Add some using the console!</p>");
        } else {
            sb.append("<table><tr><th>Cover</th><th>Title</th><th>Author</th><th>Year</th><th>Price</th><th>View</th></tr>");
            for (Book b : books.values()) {
                sb.append("<tr>");
                sb.append("<td><img src=\"").append(escape(b.coverImageUrl)).append("\" alt=\"Cover\"></td>");
                sb.append("<td>").append(escape(b.title)).append("</td>");
                sb.append("<td>").append(escape(b.author)).append("</td>");
                sb.append("<td>").append(b.publicationYear).append("</td>");
                sb.append("<td>$").append(String.format("%.2f", b.priceAmount)).append("</td>");
                sb.append("<td><a href=\"/books/").append(escape(b.id)).append("\">View Details →</a></td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
        }
        sb.append("<p><small>Add new books via the console and refresh this page to see updates.</small></p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private static String buildDetailsHtml(Book book) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>").append(escape(book.title)).append("</title>");
        sb.append("<style>body{font-family:sans-serif;margin:40px;line-height:1.6;}img{max-width:300px;float:left;margin-right:20px;}</style>");
        sb.append("</head><body>");
        sb.append("<a href=\"/books/\">← Back to List</a><hr>");
        sb.append("<h1>").append(escape(book.title)).append("</h1>");
        sb.append("<img src=\"").append(escape(book.coverImageUrl)).append("\" alt=\"Cover\">");
        sb.append("<p><strong>Author:</strong> ").append(escape(book.author)).append("</p>");
        sb.append("<p><strong>Publication Year:</strong> ").append(book.publicationYear).append("</p>");
        sb.append("<p><strong>ISBN:</strong> ").append(escape(book.isbn)).append("</p>");
        sb.append("<p><strong>Price:</strong> $").append(String.format("%.2f", book.priceAmount)).append(" ").append(book.currency).append("</p>");
        sb.append("<p><strong>Description:</strong><br>").append(escape(book.description)).append("</p>");
        sb.append("<hr><a href=\"/books/\">← Back to List</a>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private static String buildNotFoundHtml() {
        return "<html><head><title>Not Found</title></head><body><h1>Book Not Found</h1><p>Sorry, no book with that ID.</p><a href=\"/books/\">← Back to List</a></body></html>";
    }

    private static String buildJson(Book book) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"id\": \"").append(escape(book.id)).append("\",\n");
        sb.append("  \"title\": \"").append(escape(book.title)).append("\",\n");
        sb.append("  \"author\": \"").append(escape(book.author)).append("\",\n");
        sb.append("  \"publicationYear\": ").append(book.publicationYear).append(",\n");
        sb.append("  \"isbn\": \"").append(escape(book.isbn)).append("\",\n");
        sb.append("  \"description\": \"").append(escape(book.description)).append("\",\n");
        sb.append("  \"coverImageUrl\": \"").append(escape(book.coverImageUrl)).append("\",\n");
        sb.append("  \"price\": {\n");
        sb.append("    \"amount\": ").append(String.format("%.2f", book.priceAmount)).append(",\n");
        sb.append("    \"currency\": \"").append(book.currency).append("\"\n");
        sb.append("  },\n");
        sb.append("  \"averageRating\": null,\n");
        sb.append("  \"totalReviews\": 0,\n");
        sb.append("  \"reviews\": []\n");
        sb.append("}");
        return sb.toString();
    }

    private static String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("\'", "&#39;");
    }
}