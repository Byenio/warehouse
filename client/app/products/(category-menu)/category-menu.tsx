import {
    Accordion,
    AccordionContent,
    AccordionItem,
    AccordionTrigger,
} from "@/components/ui/accordion";
import { Button } from "@/components/ui/button";

export default function CategoryMenu({
    categories,
}: {
    categories: Category[];
}) {
    return (
        <div className="mx-6 my-6">
            <p className="font-bold mb-2">KATEGORIE PRODUKTÓW</p>
            <Accordion type="single" collapsible>
                {categories.map((category) => (
                    <AccordionItem key={category.id} value={category.id}>
                        <AccordionTrigger>{category.name}</AccordionTrigger>
                        <AccordionContent>
                            <Subcategories
                                subcategories={category.subcategories}
                            />
                        </AccordionContent>
                    </AccordionItem>
                ))}
            </Accordion>
        </div>
    );
}

function Subcategories({
    subcategories,
}: {
    subcategories: SubcategoryInfo[];
}) {
    return (
        <>
            {subcategories.map((subcategory) => (
                <Button key={subcategory.id} variant="link" className="w-full">
                    {subcategory.name}
                </Button>
            ))}
        </>
    );
}
