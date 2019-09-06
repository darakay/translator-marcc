package com.darakay.marcc.services;

import com.darakay.marcc.domain.Token;
import com.google.common.collect.ImmutableMap;
import org.commonmark.node.*;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class HtmlTokenVisitor extends AbstractVisitor {

    private final Tokenizer tokenizer;

    public HtmlTokenVisitor() {
        this.tokenizer = new Tokenizer();
    }

    public List<Token> getTokens(){
        return tokenizer.getTokens();
    }

    @Override
    public void visit(BlockQuote blockQuote) {
        String tagName = "blockquote";
        tokenizer.openTagWithoutAttrs(tagName);
        visitChildren(blockQuote);
        tokenizer.closeTag(tagName);
    }

    @Override
    public void visit(BulletList bulletList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Code code) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Document document) {
        visitChildren(document);
    }

    @Override
    public void visit(Emphasis emphasis) {
        String tagName = "em";
        tokenizer.openTagWithoutAttrs(tagName);
        visitChildren(emphasis);
        tokenizer.closeTag(tagName);
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        Map<String, String> attributes = new LinkedHashMap<>();
        String info = fencedCodeBlock.getInfo();
        if (info != null && !info.isEmpty()) {
            int space = info.indexOf(" ");
            String language;
            if (space == -1) {
                language = info;
            } else {
                language = info.substring(0, space);
            }
            attributes.put("class", "language-" + language);
        }
        renderCodeBlock(fencedCodeBlock, fencedCodeBlock.getLiteral(), attributes);
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        tokenizer.openTagWithoutAttrs("br");
    }

    @Override
    public void visit(Heading heading) {
        String tagName = "h"+heading.getLevel();
        tokenizer.openTagWithoutAttrs(tagName);
        visitChildren(heading);
        tokenizer.closeTag(tagName);
    }

    @Override
    public void visit(ThematicBreak thematicBreak) {
        tokenizer.openTagWithoutAttrs("hr");
    }

    @Override
    public void visit(HtmlInline htmlInline) {
        tokenizer.raw(htmlInline.getLiteral());
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
        tokenizer.raw(htmlBlock.getLiteral());

    }

    @Override
    public void visit(Image image) {
        String url = image.getDestination();

        AltTextVisitor altTextVisitor = new AltTextVisitor();
        image.accept(altTextVisitor);
        String altText = altTextVisitor.getAltText();

        Map<String, String> attrs = new LinkedHashMap<>();
        attrs.put("src", url);
        attrs.put("alt", altText);
        if (image.getTitle() != null) {
            attrs.put("title", image.getTitle());
        }

        tokenizer.selfClosedTag("img", attrs);
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        renderCodeBlock(indentedCodeBlock, indentedCodeBlock.getLiteral(), ImmutableMap.of());
    }

    @Override
    public void visit(Link link) {
        Map<String, String> attrs = new LinkedHashMap<>();
        String url = link.getDestination();
        attrs.put("href", url);
        if (link.getTitle() != null) {
            attrs.put("title", link.getTitle());
        }
        tokenizer.openTag("a",attrs);
        visitChildren(link);
        tokenizer.closeTag("a");
    }

    @Override
    public void visit(ListItem listItem) {
        tokenizer.openTagWithoutAttrs("li");
        visitChildren(listItem);
        tokenizer.closeTag("li");
    }

    @Override
    public void visit(OrderedList orderedList) {
        int start = orderedList.getStartNumber();
        Map<String, String> attrs = new LinkedHashMap<>();
        if (start != 1) {
            attrs.put("start", String.valueOf(start));
        }
        renderListBlock(orderedList, "ol", attrs);
    }

    @Override
    public void visit(Paragraph paragraph) {
        String tagName = "p";
        boolean inTightList = isInTightList(paragraph);
        if (!inTightList) {
           tokenizer.openTagWithoutAttrs(tagName);
        }
        visitChildren(paragraph);
        if (!inTightList) {
            tokenizer.closeTag(tagName);
        }
    }

    @Override
    public void visit(SoftLineBreak softLineBreak) {
        tokenizer.raw("\n");
    }

    @Override
    public void visit(StrongEmphasis strongEmphasis) {
        String strong = "strong";
        tokenizer.openTagWithoutAttrs(strong);
        visitChildren(strongEmphasis);
        tokenizer.closeTag(strong);
    }

    @Override
    public void visit(Text text) {
        tokenizer.text(text.getLiteral());
    }

    @Override
    public void visit(LinkReferenceDefinition linkReferenceDefinition) {
        String link = "a";
        tokenizer.openTagWithoutAttrs(link);
        visitChildren(linkReferenceDefinition);
        tokenizer.closeTag(link);
    }

    @Override
    public void visit(CustomBlock customBlock) {
        super.visit(customBlock);
    }

    @Override
    public void visit(CustomNode customNode) {
        super.visit(customNode);
    }

    @Override
    protected void visitChildren(Node parent) {
        super.visitChildren(parent);
    }

    private boolean isInTightList(Paragraph paragraph) {
        Node parent = paragraph.getParent();
        if (parent != null) {
            Node gramps = parent.getParent();
            if (gramps != null && gramps instanceof ListBlock) {
                ListBlock list = (ListBlock) gramps;
                return list.isTight();
            }
        }
        return false;
    }

    private void renderListBlock(ListBlock listBlock, String tagName, Map<String, String> attributes) {
        tokenizer.openTag(tagName, attributes);
        visitChildren(listBlock);
        tokenizer.closeTag(tagName);
    }

    private void renderCodeBlock(Node parent, String innerText, Map<String, String> attr){
        tokenizer.openTag("pre", ImmutableMap.of());
        tokenizer.openTag("code", attr);
        visitChildren(parent);
        tokenizer.closeTag("code");
        tokenizer.closeTag("pre");
    }

    private static class AltTextVisitor extends AbstractVisitor {

        private final StringBuilder sb = new StringBuilder();

        String getAltText() {
            return sb.toString();
        }

        @Override
        public void visit(Text text) {
            sb.append(text.getLiteral());
        }

        @Override
        public void visit(SoftLineBreak softLineBreak) {
            sb.append('\n');
        }

        @Override
        public void visit(HardLineBreak hardLineBreak) {
            sb.append('\n');
        }
    }
}
