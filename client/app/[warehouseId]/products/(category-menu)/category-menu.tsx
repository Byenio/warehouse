import {
    Accordion,
    AccordionContent,
    AccordionItem,
    AccordionTrigger,
} from "@/components/ui/accordion";
import ClearFilters from "./clear-filters";
import Subcategories from "./subcategories";

export default async function CategoryMenu({
    categories,
    warehouseId,
}: {
    categories: Category[];
    warehouseId: string;
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
            <ClearFilters warehouseId={warehouseId} />
        </div>
    );
}
